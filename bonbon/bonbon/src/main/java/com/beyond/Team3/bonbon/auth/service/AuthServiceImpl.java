package com.beyond.Team3.bonbon.auth.service;


import com.beyond.Team3.bonbon.auth.JwtTokenProvider;
import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.auth.dto.UserLoginDto;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    // 로그인 로직
    @Override
    @Transactional
    public JwtToken signIn(UserLoginDto userLoginDto) {

        // email, password 기반 Authentication 객체 생성
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword());

        // 검증 진행 -> authenticationManagerBuilder 이용
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(auth);

        String accessToken = jwtTokenProvider.createToken(userLoginDto.getEmail(), authentication.getAuthorities(), jwtTokenProvider.getAccessTokenExpirationTime());
        String refreshToken = jwtTokenProvider.createRefreshToken(userLoginDto.getEmail());

        // 위의 과정을 통과할 경우 -> AccessToken + RefreshToken 발급
        return new JwtToken(accessToken, refreshToken);
    }

    // 로그아웃 로직
    @Override
    public void logout(String token){
        // token에서 access Token만 추출
        String accessToken = jwtTokenProvider.resolveToken(token);

        // 추출한 토큰 유효성 확인
        if(!jwtTokenProvider.validateToken(accessToken)){
            throw new UserException(ExceptionMessage.INVALID_ACCESS_TOKEN);
        };

        // access Token을 BlackList에 담고
        jwtTokenProvider.addBlackList(accessToken);

        // RefreshToken은 삭제하면 됨.
        jwtTokenProvider.deleteRefreshToken(accessToken);
    }


    // 토큰 재발급 로직
    // 일반적으로 만료 되기 5분 정도 전에 물어보는 방식으로 가야할 것 같음
    @Override
    public JwtToken refresh(String token) {

        // refresh 토큰 추출
        String refreshToken = jwtTokenProvider.resolveToken(token);

        // 해당 토큰이 유효한지 확인 -> RefreshToken 이 이미 만료된 경우 재발급 불가
        if(refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)){
            throw new UserException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        };

        // 추출한 토큰에서 사용자 추출
        User user = userRepository.findByEmail(jwtTokenProvider.getUserName(refreshToken))
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        Collection<GrantedAuthority> authorities = user.getUserType() != null ?
                    List.of(new SimpleGrantedAuthority(user.getUserType().name())) :
                    List.of();

        // Refresh는 이전에 쓰던거 그대로 유지
        return new JwtToken(
                jwtTokenProvider.createToken(user.getEmail(), authorities, jwtTokenProvider.getAccessTokenExpirationTime()),
                refreshToken
        );
    }

}

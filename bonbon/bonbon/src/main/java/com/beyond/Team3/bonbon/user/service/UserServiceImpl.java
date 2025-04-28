package com.beyond.Team3.bonbon.user.service;

import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserRegisterDto;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 계정 생성 -> only HEADQUARTER만
    @Transactional
    public void join(UserRegisterDto userRegisterDto) {

        // email 중복 확인
        checkEmailDuplication(userRegisterDto.getEmail());

        // 비밀번호, 비밀번호 확인 일치
        if (!userRegisterDto.passwordMatching()) {
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }

        // 사용자 생성, 비밀번호 encoding
        User user = userRegisterDto.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    // 사용자 정보 조회
    @Transactional
    public UserInfoDto getUser(Principal principal) {

        String userEmail = principal.getName();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        UserInfoDto userInfoDto = new UserInfoDto(user);

        return userInfoDto;
    }

    // 비밀번호 변경
    @Transactional
    public void modifyPassword(Principal principal, PasswordModifyDto passwordModifyDto) {

        String userEmail = principal.getName();

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserException(ExceptionMessage.USER_NOT_FOUND));

        // 이전 비밀번호 일치 / 새로운 비밀번호, 비밀번호 확인이 일치하는지 확인
        if(!passwordEncoder.matches(passwordModifyDto.getOldPassword(), user.getPassword())
                || !passwordEncoder.matches(passwordModifyDto.getNewPassword(), passwordModifyDto.getNewPasswordConfirm()))
        {
            throw new UserException(ExceptionMessage.PASSWORD_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(passwordModifyDto.getNewPassword()));
    }

    // 가입하려는 email이 이미 존재하는 이메일인지 확인
    public void checkEmailDuplication(String email){

        Optional<User> findUsers = userRepository.findByEmail(email);
        if(findUsers.isPresent()){
            throw new UserException(ExceptionMessage.EMAIL_ALREADY_EXIST);
        }
    }


}

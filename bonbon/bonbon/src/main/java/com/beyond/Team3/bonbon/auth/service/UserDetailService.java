package com.beyond.Team3.bonbon.auth.service;

import com.beyond.Team3.bonbon.user.entity.PrincipalDetails;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalInt;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // 사용자 존재 여부 확인
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        if(user.isPresent()) {
            return new PrincipalDetails(user.get());
        }

        // 사용자 없으면 예외 발생
        throw new UsernameNotFoundException("해당 이메일을 갖는 사용자가 존재하지 않습니다: " + username);
    }
}

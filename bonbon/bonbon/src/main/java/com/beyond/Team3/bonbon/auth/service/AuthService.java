package com.beyond.Team3.bonbon.auth.service;


import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.auth.dto.UserLoginDto;
import org.springframework.stereotype.Service;


public interface AuthService {

    JwtToken signIn(UserLoginDto userLoginDto);  // 로그인
    JwtToken refresh(String refreshToken);     // refresh 토큰으로 새로운 액세스 토큰 발급
    void logout(String token);  // 로그아웃

}

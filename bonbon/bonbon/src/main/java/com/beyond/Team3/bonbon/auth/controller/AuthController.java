package com.beyond.Team3.bonbon.auth.controller;


import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.auth.dto.UserLoginDto;
import com.beyond.Team3.bonbon.auth.service.AuthService;
import com.beyond.Team3.bonbon.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bonbon/user")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "로그인/로그아웃")
public class AuthController {

    private final AuthService authService;

    // JWT 로그인
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일, 비밀번호로 로그인한다.")
    public ResponseEntity<JwtToken> signIn(
            @Valid @RequestBody UserLoginDto userLoginDto){

        JwtToken jwtToken = authService.signIn(userLoginDto);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그 아웃", description = "AccessToken 정보를 바탕으로 로그아웃 한다.")
    public ResponseEntity<Void> logOut(
            @RequestHeader("Authorization") String token
    ){
        authService.logout(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "AccessToken을 Refresh Token 정보를 바탕으로 재발급한다.")
    public ResponseEntity<JwtToken> refresh(
            @RequestHeader("Authorization") String token
    ){
        JwtToken refreshToken = authService.refresh(token);
        return ResponseEntity.ok(refreshToken);
    }

}

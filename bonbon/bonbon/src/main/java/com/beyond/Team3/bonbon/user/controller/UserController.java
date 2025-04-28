package com.beyond.Team3.bonbon.user.controller;


import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.auth.dto.UserLoginDto;
import com.beyond.Team3.bonbon.auth.service.AuthService;
import com.beyond.Team3.bonbon.auth.service.AuthServiceImpl;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserRegisterDto;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/bonbon/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "회원 관리")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

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

    @PostMapping("/join")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "계정 등록", description = "Headquarter만 사용자 계정 등록")
    public ResponseEntity<Void> join(
        @RequestBody UserRegisterDto userRegisterDto
    ){
        userService.join(userRegisterDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/")
    @Operation(summary = "계정 정보 조회", description = "인증이 완료된 회원 개인의 정보를 조회한다.")
    public ResponseEntity<UserInfoDto> userInfo(Principal principal) {

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        UserInfoDto userInfo = userService.getUser(principal);

        return ResponseEntity.ok(userInfo);
    }

    @PostMapping("/password")
    @Operation(summary = "비밀번호 수정", description = "인증이 완료된 회원의 비밀번호를 수정한다.")
    public ResponseEntity<String> userModifyPassword(
            Principal principal,
            @RequestBody PasswordModifyDto passwordModifyDto
    ){
        userService.modifyPassword(principal, passwordModifyDto);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }
}

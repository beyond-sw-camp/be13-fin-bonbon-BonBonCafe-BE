package com.beyond.Team3.bonbon.user.controller;


import com.beyond.Team3.bonbon.auth.dto.JwtToken;
import com.beyond.Team3.bonbon.auth.dto.UserLoginDto;
import com.beyond.Team3.bonbon.auth.service.AuthService;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerInfoDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfo;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import com.beyond.Team3.bonbon.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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

    @PostMapping("/manager")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "MANAGER 계정 등록", description = "Headquarter만 MANAGER 계정 등록")
    public ResponseEntity<String> join(
            @RequestBody ManagerRegisterDto managerRegisterDto, Principal principal
    ){
        userService.joinManager(managerRegisterDto, principal);
        return ResponseEntity.ok("Manager 계정 생성이 완료되었습니다.");
    }

    @PostMapping("/franchisee")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "FRANCHISEE 계정 등록", description = "Headquarter만 FRANCHISEE 계정 등록")
    public ResponseEntity<String> join(
            @RequestBody FranchiseeRegisterDto franchiseeRegisterDto, Principal principal
    ){
        userService.joinFranchisee(franchiseeRegisterDto, principal);
        return ResponseEntity.ok("Manager 계정 생성이 완료되었습니다.");
    }

    @GetMapping("/mamager")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 MANAGER 계정 전체 조회", description = "Headquarter에서 등록한 MANAGER 계정 정보를 확인한다.")
    public ResponseEntity<List<UserInfoDto>> mamagerAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Principal principal){
        Page<UserInfoDto> accounts = userService.getAccountsByRole(page, size, Role.MANAGER, principal);
        return ResponseEntity.ok(accounts.getContent());
    }

    @GetMapping("/franchisee")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 Franchisee 계정 전체 조회", description = "Headquarter에서 등록한 Franchisee 계정 정보를 확인한다.")
    public ResponseEntity<List<UserInfoDto>> franchiseAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Principal principal){
        Page<UserInfoDto> accounts = userService.getAccountsByRole(page, size, Role.FRANCHISEE, principal);
        return ResponseEntity.ok(accounts.getContent());
    }

    @GetMapping("/mamager/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 MANAGER 계정 단일 조회", description = "Headquarter에서 등록한 MANAGER 계정 정보를 확인한다.")
    public ResponseEntity<ManagerInfoDto> managerAccountDetail(
            @PathVariable("userId") Long userId,
            Principal principal){

        ManagerInfoDto managerDetail = userService.getManagerDetail(userId, principal);

        return ResponseEntity.ok(managerDetail);
    }

    @GetMapping("/franchisee/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 FRANCHISEE 계정 단일 조회", description = "Headquarter에서 등록한 FRANCHISEE 계정 정보를 확인한다.")
    public ResponseEntity<ManagerInfoDto> franchiseeAccountDetail(
            @PathVariable("userId") Long userId,
            Principal principal){

        ManagerInfoDto managerDetail = userService.getFranchiseeDetail(userId, principal);

        return ResponseEntity.ok(managerDetail);
    }

    @GetMapping("/")
    @Operation(summary = "계정 정보 조회", description = "인증이 완료된 회원 개인의 정보를 조회한다.")
    public ResponseEntity<UserInfo> userInfo(Principal principal) {

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        UserInfo userInfo = userService.getUser(principal);

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

    @PostMapping("/")
    @Operation(summary = "회원 개인 정보 수정", description = "회원의 전화번호, 이름, (사진) 수정 가능")
    public ResponseEntity<String> userModify(
            Principal principal,
            @RequestBody UserModifyDto userModifyDto){

        userService.update(principal, userModifyDto);
        return ResponseEntity.ok("회원 정보 수정이 완료되었습니다.");
    }

    @PostMapping("/accounts/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 특정 계정 수정", description = "Headquarter에서 등록한 단일 계정 정보를 수정한다.")
    public ResponseEntity<String> accountsModify(
            @PathVariable("userId") Long userId,
            @RequestBody UserModifyDto userModifyDto,
            Principal principal) {

        userService.registUserUpdate(userId, userModifyDto, principal);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    @GetMapping("/accounts/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 특정 계정 조회", description = "Headquarter에서 등록한 단일 계정 정보를 조회한다.")
    public ResponseEntity<UserInfo> accountsDetails(
            @PathVariable("userId") Long userId,
            Principal principal) {

        UserInfo userInfoDto = userService.getAccountDetail(userId, principal);

        return ResponseEntity.ok(userInfoDto);
    }

    @DeleteMapping("/accounts/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 특정 계정 삭제", description = "Headquarter에서 등록한 단일 계정 정보를 삭제한다.")
    public ResponseEntity<String> deleteAccount(
            @PathVariable("userId") Long userId,
            Principal principal) {

        userService.deleteUser(userId, principal);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }


}

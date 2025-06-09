package com.beyond.Team3.bonbon.user.controller;

import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.handler.exception.PageException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterResponseDto;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquarter.repository.HeadquarterRepository;
import com.beyond.Team3.bonbon.region.entity.Region;
import com.beyond.Team3.bonbon.region.repository.RegionRepository;
import com.beyond.Team3.bonbon.user.dto.FranchiseInfoDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseFilterDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeInfoDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeModifyDto;
import com.beyond.Team3.bonbon.user.dto.FranchiseeRegisterDto;
import com.beyond.Team3.bonbon.user.dto.ManagerInfoDto;
import com.beyond.Team3.bonbon.user.dto.ManagerModifyDto;
import com.beyond.Team3.bonbon.user.dto.ManagerRegisterDto;
import com.beyond.Team3.bonbon.user.dto.PasswordModifyDto;
import com.beyond.Team3.bonbon.user.dto.UserInfo;
import com.beyond.Team3.bonbon.user.dto.UserInfoDto;
import com.beyond.Team3.bonbon.user.dto.UserModifyDto;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import com.beyond.Team3.bonbon.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    private final UserService userService;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final HeadquarterRepository headquarterRepository;

    @PostMapping("/manager")
    @Operation(summary = "MANAGER 계정 등록", description = "Headquarter만 MANAGER 계정 등록")
    public ResponseEntity<String> join(
            @RequestBody ManagerRegisterDto managerRegisterDto,
            Principal principal
    ){
        userService.joinManager(managerRegisterDto);
        return ResponseEntity.ok("Manager 계정 생성이 완료되었습니다.");
    }

    @GetMapping("/manager")
//    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 MANAGER 계정 전체 조회", description = "Headquarter에서 등록한 MANAGER 계정 정보를 확인한다.")
    public ResponseEntity<Page<ManagerInfoDto>> managerAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Principal principal){
        Page<ManagerInfoDto> accounts = userService.getManagerAccounts(page, size, principal);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/manager/franchises")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "해당 지역의 가맹점 목록 조회", description = "Headquarter에서 등록한 가맹점들 목록 조회3")
    public ResponseEntity<Page<FranchiseFilterDto>> franchiseInRegion(
            @RequestParam int regionCode,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Principal principal){
        Page<FranchiseFilterDto> franchiseListInRegion = userService.getFranchiseInRegion(regionCode, page, size, principal);
        return ResponseEntity.ok(franchiseListInRegion);
    }

    @GetMapping("/manager/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 MANAGER 계정 단일 조회", description = "Headquarter에서 등록한 MANAGER 계정 정보를 확인한다.")
    public ResponseEntity<ManagerInfoDto> managerAccountDetail(
            @PathVariable("userId") Long userId,
            Principal principal){

        ManagerInfoDto managerDetail = userService.getManagerDetail(userId, principal);

        return ResponseEntity.ok(managerDetail);
    }

    @DeleteMapping("/manager/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 MANAGER 단일 계정 삭제", description = "Headquarter에서 등록한 MANAGER 계정을 삭제한다.")
    public ResponseEntity<String> deleteManagerAccount(
            @PathVariable("userId") Long userId,
            Principal principal){

        userService.deleteManager(userId, principal);

        return ResponseEntity.ok("MANAGER 계정이 삭제되었습니다.");
    }

    @GetMapping("/region")
    @Operation(summary = "등록한 MANAGER 단일 계정 삭제", description = "Headquarter에서 등록한 MANAGER 계정을 삭제한다.")
    public ResponseEntity<Page<Region>> getRegionList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        if(page < 0 || size <= 0){
            throw new PageException(ExceptionMessage.INVALID_PAGE_PARAMETER);
        }
        Page<Region> regions = regionRepository.findAll(pageable);

        return ResponseEntity.ok(regions);
    }

    @PostMapping("/franchisee")
    @Operation(summary = "FRANCHISEE 계정 등록", description = "Headquarter만 FRANCHISEE 계정 등록")
    public ResponseEntity<String> join(
            @RequestBody FranchiseeRegisterDto franchiseeRegisterDto
    ){
        userService.joinFranchisee(franchiseeRegisterDto);
        return ResponseEntity.ok("Manager 계정 생성이 완료되었습니다.");
    }

    @GetMapping("/franchisee")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 Franchisee 계정 전체 조회", description = "Headquarter에서 등록한 Franchisee 계정 정보를 확인한다.")
    public ResponseEntity<Page<FranchiseeInfoDto>> franchiseAccounts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Principal principal){
        Page<FranchiseeInfoDto> accounts = userService.getFranchiseeAccounts(page, size, Role.FRANCHISEE, principal);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/franchisee/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 FRANCHISEE 계정 단일 조회", description = "Headquarter에서 등록한 FRANCHISEE 계정 정보를 확인한다.")
    public ResponseEntity<FranchiseeInfoDto> franchiseeAccountDetail(
            @PathVariable("userId") Long userId,
            Principal principal){

        FranchiseeInfoDto franchiseeInfoDto = userService.getFranchiseeDetail(userId, principal);

        return ResponseEntity.ok(franchiseeInfoDto);
    }

    @DeleteMapping("/franchisee/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 FRANCHISEE 계정 삭제", description = "Headquarter에서 등록한 FRANCHISEE 데이터를 삭제하고, 계정 상태를 DELETED로 바꾼다.")
    public ResponseEntity<String> deleteFanchiseeAccount(
            @PathVariable("userId") Long userId,
            Principal principal) {

        userService.deleteFranchisee(userId, principal);

        return ResponseEntity.ok("FRANCHISEE 계정이 삭제되었습니다.");

    }

//    // 가맹점주가 없는 가맹점 검색
    @GetMapping("/franchisee/without-owner")
    @Operation(summary = "가맹점주가 없는 가맹점 확인", description = "가맹점주가 없는 가맹점 리스트를 조회한다.")
    public ResponseEntity<Page<FranchiseFilterDto>> getFranchisesWithoutOwner(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "headquarterId", required = false) Long headquarterId) {

        Page<FranchiseFilterDto> franchiseWithoutOwner = userService.findFranchiseWithoutOwner(page, size, headquarterId);

        return ResponseEntity.ok(franchiseWithoutOwner);
    }

    @GetMapping("/franchisee/without-owner/principal")
    @Operation(summary = "가맹점주가 없는 가맹점 확인", description = "가맹점주가 없는 가맹점 리스트를 조회한다.")
    public ResponseEntity<Page<FranchiseFilterDto>> getFranchisesWithoutOwnerPrincipal(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Principal principal) {

        Page<FranchiseFilterDto> franchiseWithoutOwner = userService.findFranchiseWithoutOwnerPrincipal(page, size, principal);

        return ResponseEntity.ok(franchiseWithoutOwner);
    }

    @GetMapping("headquarters")
    @Operation(summary = "가맹점주가 없는 가맹점 확인", description = "가맹점주가 없는 가맹점 리스트를 조회한다.")
    public ResponseEntity<List<HeadquarterResponseDto>> getHeadquarterList() {

        List<Headquarter> allHeadquarter = headquarterRepository.findAll();
        List<HeadquarterResponseDto> responseList = allHeadquarter.stream()
                .map(HeadquarterResponseDto::from)
                .toList();

        return ResponseEntity.ok(responseList);
    }

    @GetMapping
    @Operation(summary = "본인 계정 정보 조회", description = "인증이 완료된 회원 개인의 정보를 조회한다.")
    public ResponseEntity<UserInfoDto> userInfo(Principal principal) {

        // 토큰이 유효하다면 해당 토큰에서 사용자를 추출
        UserInfoDto userInfoDto = (UserInfoDto) userService.getUser(principal);

        return ResponseEntity.ok(userInfoDto);
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

    @PostMapping
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

    // 등록한 franchisee 계정 수정 -> 본사에서만 가능 / 가맹점 등록도 가능하게 해야 할 것 같은데 아무래도
    @PostMapping("/franchisee-accounts/{franchiseeId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 가맹점주 계정 수정", description = "Headquarter에서 등록한 단일 가맹점주 계정 정보를 수정한다.")
    public ResponseEntity<String> franchiseeAccountsModify(
            @PathVariable("franchiseeId") Long franchiseeId,
            @RequestBody FranchiseeModifyDto franchiseModifyDto,
            Principal principal) {

        userService.franchiseeUpdate(franchiseeId, franchiseModifyDto, principal);
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

    @PostMapping("/manager-accounts/{managerId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 담당자 계정 수정", description = "Headquarter에서 등록한 단일 담당자 계정 정보를 수정한다.")
    public ResponseEntity<String> managerAccountsModify(
            @PathVariable("managerId") Long managerId,
            @RequestBody ManagerModifyDto managerModifyDto,
            Principal principal) {

        userService.managerUpdate(managerId, managerModifyDto, principal);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    @DeleteMapping("/accounts/{userId}")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "등록한 특정 계정 삭제", description = "Headquarter에서 등록한 단일 계정 정보를 삭제한다.")
    public ResponseEntity<String> deleteAccount(
            @PathVariable("userId") Long userId,
            Principal principal) {

        userService.deleteFranchisee(userId, principal);
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    @GetMapping("/email-check")
    @Operation(summary = "이메일 중복확인", description = "이메일 중복확인")
    public ResponseEntity<Boolean> emailCheck(
            @RequestParam String email)
    {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        boolean res = userRepository.existsByEmail(email);
        return ResponseEntity.ok(res);
    }

}

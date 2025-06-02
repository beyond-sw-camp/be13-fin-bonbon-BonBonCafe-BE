package com.beyond.Team3.bonbon.franchiseMenu.controller;

import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseMenuRequestDto;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseMenuResponseDto;
import com.beyond.Team3.bonbon.franchiseMenu.dto.FranchiseSimpleResponseDto;
import com.beyond.Team3.bonbon.franchiseMenu.service.FranchiseMenuService;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/franchise-menus")
@Tag(name = "가맹점 메뉴", description = "가맹점 메뉴")
public class FranchiseMenuController {
    private final FranchiseMenuService franchiseMenuService;

    @Operation(summary = "가맹점 메뉴 단건 조회")
    @GetMapping("/{menuId}")
    public ResponseEntity<FranchiseMenuResponseDto> getAllMenus(
            Principal principal,
            @PathVariable Long menuId
    ) {
        FranchiseMenuResponseDto franchiseMenuResponseDto = franchiseMenuService.getMenuByFranchise(principal, menuId);

        return ResponseEntity.status(HttpStatus.OK).body(franchiseMenuResponseDto);
    }

    @Operation(summary = "카테고리별 가맹점 메뉴 조회")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<FranchiseMenuResponseDto>> getMenusByCategory(
            Principal principal,
            @PathVariable Long categoryId
    ) {
        List<FranchiseMenuResponseDto> menus = franchiseMenuService.findMenusByCategory(principal, categoryId);
        return ResponseEntity.ok(menus);
    }

    @Operation(summary = "본인 가맹점의 메뉴 조회 (가맹점주 전용)")
    @GetMapping()
    public ResponseEntity<Page<MenuResponseDto>> getMyMenus(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Principal principal) {
        Page<MenuResponseDto> menus = franchiseMenuService.getAllMenusByFranchise(pageable, principal);
        return ResponseEntity.ok(menus);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "특정 가맹점 메뉴 조회 (본사 전용)")
    @GetMapping("/franchise/{franchiseId}")
    public ResponseEntity<Page<MenuResponseDto>> getMenusByFranchise(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Principal principal,
            @PathVariable Long franchiseId) {
        Page<MenuResponseDto> menus = franchiseMenuService.getMenusByFranchise(pageable, principal, franchiseId);
        return ResponseEntity.ok(menus);
    }

    @Operation(summary = "가맹점 메뉴 등록")
    @PostMapping
    public ResponseEntity<FranchiseMenuResponseDto> createFranchiseMenu(
            Principal principal,
            @RequestBody FranchiseMenuRequestDto dto) {
        FranchiseMenuResponseDto franchiseMenuResponseDto = franchiseMenuService.create(principal, dto);
        return ResponseEntity.ok(franchiseMenuResponseDto);
    }

    @Operation(summary = "가맹점 메뉴 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteFranchiseMenu(
            Principal principal,
            @RequestBody FranchiseMenuRequestDto dto) {
        franchiseMenuService.delete(principal, dto);

        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "특정 메뉴를 판매 중인 가맹점 목록 조회")
    @GetMapping("/menu/{menuId}/franchises")
    public ResponseEntity<List<FranchiseSimpleResponseDto>> getFranchisesByMenu(
            @PathVariable Long menuId
    ) {
        List<FranchiseSimpleResponseDto> franchises = franchiseMenuService.getFranchisesByMenu(menuId);
        return ResponseEntity.ok(franchises);
    }


}

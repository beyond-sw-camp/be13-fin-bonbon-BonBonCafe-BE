package com.beyond.Team3.bonbon.menu.controller;

import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.service.MenuService;
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

@Tag(name = "메뉴", description = "메뉴")
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "메뉴 전체 조회 + 검색", description = "")
    @GetMapping("/headquarters/menus")
    public ResponseEntity<Page<MenuResponseDto>> getAllMenu(
            @PageableDefault(size = 12, page = 0) Pageable pageable,
            Principal principal,
            @RequestParam(required = false) String search
    ) {
        Page<MenuResponseDto> menuResponseDto = menuService.getAllMenu(pageable, principal, search);
        return ResponseEntity.ok(menuResponseDto);
    }

    @Operation(summary = "본사가 본인들의 카테고리로 메뉴 확인")
    @GetMapping("/headquarters/categories/{categoryId}/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenusByCategoryInHeadquarter(
            Principal principal,
            @PathVariable Long categoryId
    ) {
        List<MenuResponseDto> menus = menuService.getMenusByCategoryAndHeadquarter(categoryId, principal);
        return ResponseEntity.ok(menus);
    }

    @Operation(summary = "메뉴 단일 조회", description = "본사 번호 입력")
    @GetMapping("/headquarters/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu(
            Principal principal,
            @PathVariable Long menuId
    ) {
        MenuResponseDto menuResponseDto = menuService.getMenu(menuId, principal);
        return ResponseEntity.status(HttpStatus.OK).body(menuResponseDto);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "메뉴 등록")
    @PostMapping("/headquarters/menus")
    public ResponseEntity<MenuResponseDto> createMenu(
            @RequestBody MenuRequestDto menuRequestDto,
            Principal principal
    ) {
        MenuResponseDto menuResponseDto = menuService.createMenu(menuRequestDto, principal);

        return ResponseEntity.ok(menuResponseDto);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "메뉴 수정")
    @PutMapping("/headquarters/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            Principal principal,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto menuRequestDto) {
        MenuResponseDto menuResponseDto = menuService.updateMenu(menuId, principal, menuRequestDto);

        return ResponseEntity.ok(menuResponseDto);
    }

    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    @Operation(summary = "메뉴 삭제")
    @DeleteMapping("/headquarters/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(
            Principal principal,
            @PathVariable Long menuId) {

        menuService.deleteMenu(menuId, principal);
        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }

}

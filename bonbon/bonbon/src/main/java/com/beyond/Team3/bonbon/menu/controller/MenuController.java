package com.beyond.Team3.bonbon.menu.controller;

import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "메뉴", description = "메뉴")
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @Operation(summary = "메뉴 전체 조회", description = "본사 번호 입력")
    @GetMapping("/headquarters/{headquarterId}/menus")
    public ResponseEntity<Page<MenuResponseDto>> getAllMenu(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long headquarterId
            ){
        Page<MenuResponseDto> menuResponseDto = menuService.getAllMenu(pageable, headquarterId);

        return ResponseEntity.ok(menuResponseDto);
    }

    @Operation(summary = "메뉴 단일 조회", description = "본사 번호 입력")
    @GetMapping("/headquarters/{headquarterId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> getMenu(
            @PathVariable Long headquarterId,
            @PathVariable Long menuId
    ){
        MenuResponseDto menuResponseDto = menuService.getMenu(menuId, headquarterId);

        return ResponseEntity.status(HttpStatus.OK).body(menuResponseDto);
    }

    @Operation(summary = "메뉴 등록")
    @PostMapping("/headquarters/{headquarterId}/menus")
    public ResponseEntity<MenuResponseDto> createMenu(
            @RequestBody MenuRequestDto menuRequestDto,
            @PathVariable Long headquarterId
            ){
        MenuResponseDto menuResponseDto = menuService.createMenu(menuRequestDto, headquarterId);

        return ResponseEntity.ok(menuResponseDto);
    }

    @Operation(summary = "메뉴 수정")
    @PutMapping("/headquarters/{headquarterId}/menus/{menuId}")
    public ResponseEntity<MenuResponseDto> updateMenu(
            @PathVariable Long headquarterId,
            @PathVariable Long menuId,
            @RequestBody MenuRequestDto menuRequestDto) {
        MenuResponseDto menuResponseDto = menuService.updateMenu(menuId, headquarterId, menuRequestDto);

        return ResponseEntity.ok(menuResponseDto);
    }

    @Operation(summary = "메뉴 삭제")
    @DeleteMapping("/headquarters/{headquarterId}/menus/{menuId}")
    public ResponseEntity<String> deleteMenu(
            @PathVariable Long headquarterId,
            @PathVariable Long menuId) {

        menuService.deleteMenu(menuId, headquarterId);
        return ResponseEntity.status(HttpStatus.OK).body("메뉴가 삭제되었습니다.");
    }


}

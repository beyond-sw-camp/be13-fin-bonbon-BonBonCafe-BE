package com.beyond.Team3.bonbon.menuCategory.controller;

import com.beyond.Team3.bonbon.menu.dto.MenuResponseDto;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryRequestDto;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuCategory.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리", description = "카테고리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 전체 조회")
    @GetMapping("/{categoryId}/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenusByCategory(@PathVariable Long categoryId) {
        List<MenuResponseDto> menus = categoryService.getMenusByCategory(categoryId);
        return ResponseEntity.ok(menus);
    }

    @Operation(summary = "카테고리 등록")
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto responseDto = categoryService.createCategory(requestDto);
        return ResponseEntity.ok(responseDto);
    }
}

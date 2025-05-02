package com.beyond.Team3.bonbon.headquarter.controller;


import com.beyond.Team3.bonbon.headquarter.service.HeadquarterService;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "본사", description = "본사")
@RestController
@RequiredArgsConstructor
public class HeadquarterController {

    private final HeadquarterService headquarterService;

    @Operation(summary = "본사의 카테고리 목록")
    @GetMapping("/headquarters/{headquarterId}/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesByHeadquarter(@PathVariable Long headquarterId) {
        List<CategoryResponseDto> categories = headquarterService.getCategoriesByHeadquarter(headquarterId);
        return ResponseEntity.ok(categories);
    }
}

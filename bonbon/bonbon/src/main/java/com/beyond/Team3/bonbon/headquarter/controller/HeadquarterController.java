package com.beyond.Team3.bonbon.headquarter.controller;


import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterRequestDto;
import com.beyond.Team3.bonbon.headquarter.dto.HeadquarterResponseDto;
import com.beyond.Team3.bonbon.headquarter.service.HeadquarterService;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@Tag(name = "본사", description = "본사")
@RestController
@RequiredArgsConstructor
public class HeadquarterController {

    private final HeadquarterService headquarterService;

//  본사 등록, 삭제는 넣지 않았습니다.

    @Operation(summary = "본사 단일 조회")
    @GetMapping("/headquarters")
    public ResponseEntity<HeadquarterResponseDto> getHeadquarter(
            Principal principal // 검증도 넣어야할듯??
    ) {
        HeadquarterResponseDto headquarterResponseDto = headquarterService.getHeadquarter(principal);
        return ResponseEntity.status(HttpStatus.OK).body(headquarterResponseDto);
    }

    @Operation(summary = "본사 정보 수정")
    @PutMapping("/headquarters")
    @PreAuthorize("hasRole('ROLE_HEADQUARTER')")
    public ResponseEntity<HeadquarterResponseDto> updateHeadquarter(
            Principal principal,
            @RequestBody HeadquarterRequestDto headquarterRequestDto
    ) {
        HeadquarterResponseDto headquarterResponseDto = headquarterService.updateHeadquarter(principal, headquarterRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(headquarterResponseDto);
    }

    @Operation(summary = "본사의 카테고리 목록")
    @GetMapping("/headquarters/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesByHeadquarter(Principal principal
    ) {
        List<CategoryResponseDto> categories = headquarterService.getCategoriesByHeadquarter(principal);

        return ResponseEntity.ok(categories);
    }
}

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "본사", description = "본사")
@RestController
@RequiredArgsConstructor
public class HeadquarterController {

    private final HeadquarterService headquarterService;

//  본사 등록, 삭제는 넣지 않았습니다.

    @Operation(summary = "본사 단일 조회")
    @GetMapping("/headquarters/{headquarterId}")
    public ResponseEntity<HeadquarterResponseDto> getHeadquarter(
            @PathVariable Long headquarterId // 검증도 넣어야할듯??
    ) {
        HeadquarterResponseDto headquarterResponseDto = headquarterService.getHeadquarter(headquarterId);
        return ResponseEntity.status(HttpStatus.OK).body(headquarterResponseDto);
    }

    @Operation(summary = "본사 정보 수정")
    @PutMapping("/headquarters/{headquarterId}")
    public ResponseEntity<HeadquarterResponseDto> updateHeadquarter(
            @PathVariable Long headquarterId,
            @RequestBody HeadquarterRequestDto headquarterRequestDto
    ) {
        HeadquarterResponseDto headquarterResponseDto = headquarterService.updateHeadquarter(headquarterId, headquarterRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(headquarterResponseDto);
    }

    @Operation(summary = "본사의 카테고리 목록")
    @GetMapping("/headquarters/{headquarterId}/categories")
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesByHeadquarter(@PathVariable Long headquarterId) {
        List<CategoryResponseDto> categories = headquarterService.getCategoriesByHeadquarter(headquarterId);

        return ResponseEntity.ok(categories);
    }
}

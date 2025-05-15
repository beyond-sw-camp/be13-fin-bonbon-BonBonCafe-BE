package com.beyond.Team3.bonbon.franchiseStock.controller;

import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockRequestDto;
import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockResponseDto;
import com.beyond.Team3.bonbon.franchiseStock.service.FranchiseStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "가맹점 재고", description = "가맹점 재고입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/franchise-stocks")
public class FranchiseStockController {
    private final FranchiseStockService franchiseStockService;

    @Operation(summary = "재고 단건 조회")
    @GetMapping("/me/{franchiseId}")
    public ResponseEntity<FranchiseStockResponseDto> getFranchiseStock(@PathVariable("franchiseId") Long franchiseId) {
        FranchiseStockResponseDto franchiseStockResponseDto = franchiseStockService.getFranchiseStock(franchiseId);

        return ResponseEntity.ok(franchiseStockResponseDto);
    }

    @Operation(summary = "가맹점의 재고 전체 조회")
    @GetMapping("/{franchiseId}")
    public ResponseEntity<Page<FranchiseStockResponseDto>> getAllStock(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long franchiseId
    ) {
        Page<FranchiseStockResponseDto> franchiseStockResponseDto = franchiseStockService.getAllStock(pageable, franchiseId);
        return ResponseEntity.ok(franchiseStockResponseDto);
    }

//    @Operation(summary = "재고 등록")
////    @PostMapping("/me/{franchiseId}")
//    public ResponseEntity<FranchiseStockResponseDto> createFranchiseStock(
//            @PathVariable Long franchiseId,
//            @RequestBody FranchiseStockRequestDto franchiseStockRequestDto
//    ) {
//        FranchiseStockResponseDto franchiseStockResponseDto = franchiseStockService.createFranchiseStock(franchiseId, franchiseStockRequestDto);
//
//        return ResponseEntity.ok(franchiseStockResponseDto);
//    }

    @Operation(summary = "재고 삭제")
    @DeleteMapping("/me/{franchiseId}")
    public ResponseEntity<String> deleteFranchiseStock(@PathVariable Long franchiseId) {
        franchiseStockService.deleteFranchiseStock(franchiseId);

        return ResponseEntity.status(HttpStatus.OK).body("재고가 삭제되었습니다.");
    }

    @Operation(summary = "재고 수정")
    @PutMapping("/me/{franchiseId}")
    public ResponseEntity<FranchiseStockResponseDto> updateFranchiseStock(
            @PathVariable Long franchiseId,
            @RequestBody FranchiseStockRequestDto franchiseStockRequestDto) {
        FranchiseStockResponseDto franchiseStockResponseDto = franchiseStockService.updateFranchiseStock(franchiseId, franchiseStockRequestDto);

        return ResponseEntity.ok(franchiseStockResponseDto);
    }
}

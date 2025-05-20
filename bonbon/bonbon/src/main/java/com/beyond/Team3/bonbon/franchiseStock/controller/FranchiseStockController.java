package com.beyond.Team3.bonbon.franchiseStock.controller;

import com.beyond.Team3.bonbon.franchiseStock.dto.FranchiseStockResponseDto;
import com.beyond.Team3.bonbon.franchiseStock.service.FranchiseStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "가맹점 재고", description = "가맹점 재고입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/franchise-stocks")
public class FranchiseStockController {
    private final FranchiseStockService franchiseStockService;

    @Operation(summary = "재고 단건 조회")
    @GetMapping("/me/{franchiseStockId}")
    public ResponseEntity<FranchiseStockResponseDto> getFranchiseStock(
            Principal principal,
            @PathVariable Long franchiseStockId) {
        FranchiseStockResponseDto franchiseStockResponseDto = franchiseStockService.getFranchiseStock(principal, franchiseStockId);

        return ResponseEntity.ok(franchiseStockResponseDto);
    }

    @Operation(summary = "가맹점의 재고 전체 조회")
    @GetMapping("")
    public ResponseEntity<Page<FranchiseStockResponseDto>> getAllStock(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            Principal principal
    ) {
        Page<FranchiseStockResponseDto> franchiseStockResponseDto = franchiseStockService.getAllStock(pageable, principal);
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

//    @Operation(summary = "재고 삭제")
//    @DeleteMapping("/me/{franchiseId}")
//    public ResponseEntity<String> deleteFranchiseStock(Principal principal, @PathVariable Long franchiseId) {
//        franchiseStockService.deleteFranchiseStock(principal, franchiseId);
//
//        return ResponseEntity.status(HttpStatus.OK).body("재고가 삭제되었습니다.");
//    }
//
//    @Operation(summary = "재고 수정")
//    @PutMapping("/me/{franchiseId}")
//    public ResponseEntity<FranchiseStockResponseDto> updateFranchiseStock(
//            @PathVariable Long franchiseId,
//            @RequestBody FranchiseStockRequestDto franchiseStockRequestDto) {
//        FranchiseStockResponseDto franchiseStockResponseDto = franchiseStockService.updateFranchiseStock(franchiseId, franchiseStockRequestDto);
//
//        return ResponseEntity.ok(franchiseStockResponseDto);
//    }
}

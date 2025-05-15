package com.beyond.Team3.bonbon.franchiseStockHistory.controller;

import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryRequestDto;
import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryResponseDto;
import com.beyond.Team3.bonbon.franchiseStockHistory.service.FranchiseStockHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "가맹점 주문 내역", description = "가맹점 주문 관련입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/franchiseOrder")
public class FranchiseStockHistoryController {
    private final FranchiseStockHistoryService franchiseStockHistoryService;

    @Operation(summary = "재고 신청 내역 조회")
    @GetMapping("/{franchiseId}/{historyId}")
    public ResponseEntity<FranchiseStockHistoryResponseDto> getFranchiseStockHistory(
            @PathVariable Long franchiseId,
            @PathVariable Long historyId) {
        FranchiseStockHistoryResponseDto franchiseStockHistoryResponseDto = franchiseStockHistoryService.getHistory(franchiseId, historyId);

        return ResponseEntity.status(HttpStatus.OK).body(franchiseStockHistoryResponseDto);
    }

    @Operation(summary = "가맹점의 재고 신청 내역 전체 조회 - 본사")
    @GetMapping("/headquarter/{headquarterId}/franchise-history-list")
    public ResponseEntity<Page<FranchiseStockHistoryResponseDto>> getAllFranchiseHistory(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long headquarterId) {
        Page<FranchiseStockHistoryResponseDto> franchiseStockHistoryResponseDto = franchiseStockHistoryService.getAllFranchiseHistory(pageable, headquarterId);
        return ResponseEntity.ok(franchiseStockHistoryResponseDto);
    }

    @Operation(summary = "재고 신청 내역 전체 조회 - 가맹점")
    @GetMapping("/{franchiseId}/list")
    public ResponseEntity<Page<FranchiseStockHistoryResponseDto>> getAllHistory(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long franchiseId) {
        Page<FranchiseStockHistoryResponseDto> franchiseStockHistoryResponseDto = franchiseStockHistoryService.getAllHistory(pageable, franchiseId);
        return ResponseEntity.ok(franchiseStockHistoryResponseDto);
    }

    @Operation(summary = "재고 신청", description = "가맹점이 재료를 신청합니다.")
    @PostMapping("/{headquarterId}/{franchiseId}")
    public ResponseEntity<FranchiseStockHistoryResponseDto> createFranchiseStockHistory(
            @PathVariable Long headquarterId,
            @PathVariable Long franchiseId,
            @RequestBody FranchiseStockHistoryRequestDto requestDto
    ) {
        FranchiseStockHistoryResponseDto franchiseStockHistoryResponseDto =
                franchiseStockHistoryService.createFranchiseStockHistory(headquarterId, franchiseId, requestDto);

        return ResponseEntity.ok(franchiseStockHistoryResponseDto);
    }

    @Operation(summary = "재고 신청 내역 삭제") // 본사만?
    @DeleteMapping("/{franchiseId}/{historyId}")
    public ResponseEntity<String> deleteFranchiseStockHistory(@PathVariable Long franchiseId, @PathVariable Long historyId) {
        franchiseStockHistoryService.deleteFranchiseStockHistory(franchiseId, historyId);

        return ResponseEntity.status(HttpStatus.OK).body("결제 기록이 삭제되었습니다.");
    }

    @Operation(summary = "재고 신청 내역 수정") // 본사만?
    @PutMapping("/{headquarterId}/{franchiseId}/{historyId}")
    public ResponseEntity<FranchiseStockHistoryResponseDto> updateFranchiseStockHistory(
            @PathVariable Long headquarterId,
            @PathVariable Long franchiseId,
            @PathVariable Long historyId,
            @RequestBody FranchiseStockHistoryRequestDto requestDto) {
        FranchiseStockHistoryResponseDto franchiseStockHistoryResponseDto =
                franchiseStockHistoryService.updateHistory(headquarterId, franchiseId, historyId, requestDto);
        return ResponseEntity.ok(franchiseStockHistoryResponseDto);
    }
}
package com.beyond.Team3.bonbon.headquaterStock.controller;

import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockRequestDto;
import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockResponseDto;
import com.beyond.Team3.bonbon.headquaterStock.service.HeadquarterStockService;
import com.beyond.Team3.bonbon.ingredient.dto.IngredientRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "본사 재고", description = "본사 재고입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/headquarter-stocks")
public class HeadquarterStockController {
    private final HeadquarterStockService headquarterStockService;

    @Operation(summary = "재고 단건 조회")
    @GetMapping("/me/{headquarterStockId}")
    public ResponseEntity<HeadquarterStockResponseDto> getHeadquarterStock(@PathVariable Long headquarterStockId) {
        HeadquarterStockResponseDto headquarterStockResponseDto = headquarterStockService.getHeadquarterStock(headquarterStockId);

        return ResponseEntity.status(HttpStatus.OK).body(headquarterStockResponseDto);
    }

    @Operation(summary = "본사의 재고 전체 조회")
    @GetMapping("/{headquarterId}") //headquarterId 제거
    public ResponseEntity<Page<HeadquarterStockResponseDto>> getAllStock(
            @PageableDefault(size = 10, page = 0) Pageable pageable,
            @PathVariable Long headquarterId,
            @RequestParam(required = false) String search
    ) {
        Page<HeadquarterStockResponseDto> headquarterStockResponseDto = headquarterStockService.getAllStock(pageable, headquarterId, search);
        return ResponseEntity.ok(headquarterStockResponseDto);
    }

    @Operation(summary = "재고 등록")
    @PostMapping("/me/{headquarterId}") //headquarterId 제거
    public ResponseEntity<HeadquarterStockResponseDto> createHeadquarterStock(
            @PathVariable Long headquarterId,
            @RequestBody HeadquarterStockRequestDto headquarterStockRequestDto) {
        HeadquarterStockResponseDto headquarterStockResponseDto = headquarterStockService.createHeadquarterStock(headquarterId, headquarterStockRequestDto);

        return ResponseEntity.ok(headquarterStockResponseDto);
    }

    @Operation(summary = "재고 삭제")
    @DeleteMapping("/me/{headquarterStockId}")
    public ResponseEntity<String> deleteHeadquarterStock(@PathVariable Long headquarterStockId) {
        headquarterStockService.deleteHeadquarterStock(headquarterStockId);

        return ResponseEntity.status(HttpStatus.OK).body("재고가 삭제되었습니다.");
    }

    @Operation(summary = "재고 수정")
    @PutMapping("/me/{headquarterStockId}")
    public ResponseEntity<HeadquarterStockResponseDto> updateHeadquarterStock(
            @PathVariable Long headquarterStockId,
            @RequestBody HeadquarterStockRequestDto headquarterStockRequestDto) {
        HeadquarterStockResponseDto headquarterStockResponseDto = headquarterStockService.updateHeadquarterStock(headquarterStockId, headquarterStockRequestDto);

        return ResponseEntity.ok(headquarterStockResponseDto);
    }

    @Operation(summary = "본사 재고의 재료 목록 조회")
    @GetMapping("/{headquarterId}/ingredients")
    public ResponseEntity<List<IngredientRequestDto>> getIngredientsByHeadquarter(@PathVariable Long headquarterId) {
        List<IngredientRequestDto> ingredients = headquarterStockService.getIngredientList(headquarterId);
        return ResponseEntity.ok(ingredients);
    }

}

package com.beyond.Team3.bonbon.sales.controller;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
@Tag(name = "Franchise", description = "가맹점 관련 기능")
public class SalesController {

    private final SalesService salesService;

    @GetMapping("sales/{franchiseId}")
    @Operation(summary = "가맹점 일 매출 조회", description = "가맹점별 일 매출을 조회한다.")
    public ResponseEntity<DailySalesDto> getDailySales(
            @PathVariable("franchiseId") Long franchiseId,
            @RequestParam("salesDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate salesDate
    ) {
        DailySalesDto dailySales = salesService.getDailySales(franchiseId, salesDate);
        return  ResponseEntity.ok(dailySales);
    }







}

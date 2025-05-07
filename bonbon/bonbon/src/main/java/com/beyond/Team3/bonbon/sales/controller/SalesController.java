package com.beyond.Team3.bonbon.sales.controller;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.MenuRankingDto;
import com.beyond.Team3.bonbon.sales.dto.SalesRankingDto;
import com.beyond.Team3.bonbon.sales.service.SalesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
@Tag(name = "매출", description = "매출 관련 기능")
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

    @GetMapping("sales/period/{franchiseId}")
    @Operation(summary = "가맹점 기간별 매출 조회", description = "가맹점별 기간을 설정해서 일 매출을 조회한다.")
    public ResponseEntity<List<DailySalesDto>> getPeriodSales(
            Principal principal,
            @PathVariable("franchiseId") Long franchiseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        List<DailySalesDto> results = salesService.getPeriodSales(principal, franchiseId, startDate, endDate);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/ranking")
    @Operation(summary = "매출 순위 조회", description = "지정한 지역의 연도/월을 설정해서 매출 순위를 조회한다.")
    public ResponseEntity<Page<SalesRankingDto>> getSalesRanking(
            Principal principal,
            @RequestParam int regionCode,
            @RequestParam Integer year,
            @RequestParam(required = false) Integer month,// 월은 선택(미선택 시 연 단위로 조회 가능)
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<SalesRankingDto> franchiseRanking = salesService.getFranchiseRanking(principal, regionCode, year, month, page, size);

        return ResponseEntity.ok(franchiseRanking);
    }

    @GetMapping("/menu/ranking/{franchiseId}")
    @Operation(summary = "메뉴별 판매 순위 조회", description = "기간 설정해서 해당 지점의 메뉴 판매 순위를 상위 5개만 반환한다.")
    public ResponseEntity<List<MenuRankingDto>> getMenuSalesRanking(
            Principal principal,
            @PathVariable("franchiseId") Long franchiseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<MenuRankingDto> results = salesService.getMenuSalesRanking(principal, franchiseId, startDate, endDate);
        return ResponseEntity.ok(results);
    }



}

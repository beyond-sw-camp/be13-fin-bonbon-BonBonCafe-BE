package com.beyond.Team3.bonbon.sales.controller;

import com.beyond.Team3.bonbon.Python.dto.ForecastResponseDto;
import com.beyond.Team3.bonbon.Python.service.FlaskService;
import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.MenuRankingDto;
import com.beyond.Team3.bonbon.sales.dto.SalesRankingDto;
import com.beyond.Team3.bonbon.sales.service.SalesService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/franchises")
@RequiredArgsConstructor
@Tag(name = "매출", description = "매출 관련 기능")
public class SalesController {

    private final SalesService salesService;
    private final FlaskService flaskService;

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
    @Operation(summary = "가맹점 기간별 매출 조회", description = "가맹점별 기간을 설정해서 매출을 조회한다.")
    public ResponseEntity<List<DailySalesDto>> getPeriodSales(
            Principal principal,
            @PathVariable("franchiseId") Long franchiseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DailySalesDto> results = salesService.getPeriodSales(franchiseId, startDate, endDate);
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
    @Operation(summary = "가맹점 메뉴별 판매 순위 조회", description = "기간 설정해서 해당 지점의 메뉴 판매 순위를 조회한다.(default = 7")
    public ResponseEntity<List<MenuRankingDto>> getMenuSalesRanking(
            Principal principal,
            @PathVariable("franchiseId") Long franchiseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {


        List<MenuRankingDto> results = salesService.getMenuSalesRanking(principal, franchiseId, startDate, endDate);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/forecast/weekly/{franchiseId}")
    @Operation(summary = "가맹점 기간별 예상매출 조회", description = "일주일 단위로 예상매출을 조회한다.")
    public ResponseEntity<List<ForecastResponseDto>> getWeeklyForecast(
            @PathVariable("franchiseId") Long franchiseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectationStartDate,
            @RequestParam(defaultValue = "7") int periods) throws JsonProcessingException {
        // 과거 데이터 조회(3년 치)
        LocalDate pastYear = expectationStartDate.minusYears(3);
        List<DailySalesDto> history =
                salesService.getHistory(franchiseId, pastYear, expectationStartDate)
                        .stream().filter(d -> d.getTotalAmount() > 0)
                        .collect(Collectors.toList());

        // 에러 확인용
//        log.info(">>>>> Forecast history size={} for storeId={}", history.size(), franchiseId);
//        history.forEach(d -> log.info("  ds={}, y={}", d.getSalesDate(), d.getTotalAmount()));

        List<ForecastResponseDto> forecast =
                flaskService.getFranchiseForecast(franchiseId, history, periods);

        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/all/forecast")
    @Operation(
            summary = "본사 전체 가앵점 예상 매출 조회",
            description = "지정한 기간 모든 가맹점의 예상 매출을 조회한다."
    )
    public ResponseEntity<List<ForecastResponseDto>> getHeadQuarterForecast (
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectationStartDate,
            @RequestParam(defaultValue = "90") int periods) throws JsonProcessingException {

        // 과거 데이터 조회(3년 치)
        LocalDate pastYear = expectationStartDate.minusYears(3);
        List<DailySalesDto> history =
                salesService.getAllFranchisePeriodSales(pastYear, expectationStartDate)
                        .stream().filter(d -> d.getTotalAmount() > 0)
                        .collect(Collectors.toList());

        List<ForecastResponseDto> forecast =
                flaskService.getGlobalForecast(history, periods);

        return ResponseEntity.ok(forecast);
    }


    @GetMapping("/all/sales/period")
    @Operation(
            summary = "본사 전체 가맹점 기간별 매출 조회",
            description = "지정한 기간 동안 모든 가맹점의 매출을 날짜별로 합산하여 조회한다."
    )
    public ResponseEntity<List<DailySalesDto>> getHeadQuarterSalesByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<DailySalesDto> result =
                salesService.getAllFranchisePeriodSales(startDate, endDate);

        return ResponseEntity.ok(result);
    }

    @GetMapping("all/menu/ranking")
    @Operation(
            summary = "본사 전체 가맹점 메뉴 판매 순위 조회",
            description = "지정한 기간 동안 전국 가맹점의 메뉴 판매 순위를 합산하여 조회한다."
    )
    public ResponseEntity<List<MenuRankingDto>> getHeadQuarterMenuRanking(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<MenuRankingDto> result =
                salesService.getAllMenuSalesRanking(startDate, endDate);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/all/ranking")
    @Operation(
            summary = "본사 전체 가맹점 상위 매출 순위 조회",
            description = "지정한 기간 동안 전국 가맹점 매출 랭킹을 조회한다."
    )
    public ResponseEntity<List<SalesRankingDto>> getTopFranchiseRanking(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "7") int limit
    ) {
        List<SalesRankingDto> result = salesService.getAllFranchiseRanking(startDate, endDate, limit);

        return ResponseEntity.ok(result);
    }


}

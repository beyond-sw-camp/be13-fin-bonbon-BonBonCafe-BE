package com.beyond.Team3.bonbon.sales.service;

import com.beyond.Team3.bonbon.franchise.dto.FranchiseDto;
import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.MenuRankingDto;
import com.beyond.Team3.bonbon.sales.dto.SalesRankingDto;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface SalesService {

    // 가맹점 일 매출 조회
    DailySalesDto getDailySales(Long franchiseId, LocalDate salesDate);

    // 가맹점 기간별 매출 조회
    List<DailySalesDto> getPeriodSales(Long franchiseId, LocalDate startDate, LocalDate endDate);

    // 매출 순위 조회
    Page<SalesRankingDto> getFranchiseRanking(Principal principal, int regionCode, Integer year, Integer month, int page, int size);

    // 가맹점 기간별 판매 메뉴 순위 조회
    List<MenuRankingDto> getMenuSalesRanking(Principal principal, Long franchiseId, LocalDate startDate, LocalDate endDate);

    // 과거 데이터 조회
    List<DailySalesDto> getHistory(Long franchiseId, LocalDate startDate, LocalDate endDate);

    // 전국 가맹점 일자별 매출 합 조회
    List<DailySalesDto> getAllFranchisePeriodSales(LocalDate startDate, LocalDate endDate);

    // 전국 가맹점 메뉴별 판매량 조회
    List<MenuRankingDto> getAllMenuSalesRanking(LocalDate startDate, LocalDate endDate);

    // 전국 가맹점 매출 순위 조회
    List<SalesRankingDto> getAllFranchiseRanking(LocalDate startDate, LocalDate endDate, int limit);

}



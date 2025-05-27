package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.DailyTotalDto;
import com.beyond.Team3.bonbon.sales.dto.SalesRankingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SalesRecordRepositoryCustom {


    List<DailySalesDto> getDailySalesByPeriod(Long franchiseId, LocalDate startDate, LocalDate endDate);

    Page<SalesRankingDto> getFranchiseRanking(int regionCode, Integer year, Integer month, Pageable pageable);

    // 모든 가맹점 대상 매출 조회
    List<DailySalesDto> findAllFranchiseDailySalesByPeriod(LocalDate startDate, LocalDate endDate);

    // 모든 가맹점 대상 매출 순위
    List<SalesRankingDto> findAllFranchiseRanking(LocalDate startDate, LocalDate endDate, int limit);

}

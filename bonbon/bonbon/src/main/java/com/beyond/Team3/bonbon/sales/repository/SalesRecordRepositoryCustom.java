package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.SalesRankingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface SalesRecordRepositoryCustom {

    List<DailySalesDto> getDailySalesByPeriod(Long franchiseId, LocalDate startDate, LocalDate endDate);

    Page<SalesRankingDto> getFranchiseRanking(int regionCode, Integer year, Integer month, Pageable pageable);
}

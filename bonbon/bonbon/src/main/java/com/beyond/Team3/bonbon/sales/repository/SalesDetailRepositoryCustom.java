package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.sales.dto.DailyTotalDto;
import com.beyond.Team3.bonbon.sales.dto.MenuRankingDto;

import java.time.LocalDate;
import java.util.List;

public interface SalesDetailRepositoryCustom {
    List<DailyTotalDto> findDailyTotalsByDate(LocalDate date);

    List<MenuRankingDto> findMenuRanking(Long franchiseId, LocalDate startDate, LocalDate endDate, int limit);

}

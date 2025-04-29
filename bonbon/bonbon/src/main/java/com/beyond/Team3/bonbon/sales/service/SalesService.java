package com.beyond.Team3.bonbon.sales.service;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;

import java.time.LocalDate;

public interface SalesService {

    // 가맹점 일 매출 조회
     DailySalesDto getDailySales(Long franchiseId, LocalDate salesDate);
}

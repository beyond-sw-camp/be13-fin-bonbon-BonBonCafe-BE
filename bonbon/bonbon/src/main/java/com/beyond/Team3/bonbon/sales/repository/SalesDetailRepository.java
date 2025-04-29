package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.sales.entity.SalesDetail;
import com.beyond.Team3.bonbon.sales.entity.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SalesDetailRepository extends JpaRepository<SalesDetail, Long> {

    List<SalesDetail> findBySalesRecordAndSalesDate(SalesRecord salesRecord, LocalDate salesDate);
}

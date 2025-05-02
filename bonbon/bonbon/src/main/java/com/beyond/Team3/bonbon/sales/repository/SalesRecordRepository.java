package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.sales.entity.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {

    Optional<SalesRecord> findByFranchiseAndSalesDate(Franchise franchise, LocalDate salesDate);

}

package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.sales.entity.SalesDetail;
import com.beyond.Team3.bonbon.sales.entity.SalesDetailId;
import com.beyond.Team3.bonbon.sales.entity.SalesRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface SalesDetailRepository extends JpaRepository<SalesDetail, SalesDetailId>, SalesDetailRepositoryCustom {

    List<SalesDetail> findBySalesDetailId_FranchiseIdAndSalesDetailId_SalesDate(Long franchiseId, LocalDate SalesDate);


}

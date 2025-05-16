package com.beyond.Team3.bonbon.franchiseStock.repository;

import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FranchiseStockRepositoryCustom {
    Page<FranchiseStock> getAllStock(Pageable pageable, Long franchiseId);
}
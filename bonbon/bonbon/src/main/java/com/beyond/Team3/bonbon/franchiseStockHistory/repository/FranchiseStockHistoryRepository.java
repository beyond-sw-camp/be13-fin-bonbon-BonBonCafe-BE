package com.beyond.Team3.bonbon.franchiseStockHistory.repository;

import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FranchiseStockHistoryRepository extends JpaRepository<FranchiseStockHistory, Long>, FranchiseStockHistoryRepositoryCustom {
    Page<FranchiseStockHistory> getAllHistory(Pageable pageable, Long franchiseId);

    Page<FranchiseStockHistory> getAllFranchiseHistory(Pageable pageable, Long headquarterId);
}

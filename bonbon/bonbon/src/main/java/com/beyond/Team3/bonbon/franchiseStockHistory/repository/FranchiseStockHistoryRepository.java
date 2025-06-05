package com.beyond.Team3.bonbon.franchiseStockHistory.repository;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FranchiseStockHistoryRepository extends JpaRepository<FranchiseStockHistory, Long>, FranchiseStockHistoryRepositoryCustom {
    Page<FranchiseStockHistory> getAllHistory(Pageable pageable, Long franchiseId);

    Page<FranchiseStockHistory> getAllFranchiseHistory(Pageable pageable, Long headquarterId, HistoryStatus historyStatus);

    Page<FranchiseStockHistory> findAllByFranchiseId_FranchiseIdAndHistoryStatus(Long franchiseId, HistoryStatus status, Pageable pageable);

    Page<FranchiseStockHistory> getAllFranchiseHistoryByFranchiseIds(Pageable pageable, List<Long> franchiseIds, HistoryStatus status);
}

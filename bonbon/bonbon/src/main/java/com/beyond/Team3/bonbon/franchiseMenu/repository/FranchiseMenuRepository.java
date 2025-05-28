package com.beyond.Team3.bonbon.franchiseMenu.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchiseMenu.entity.FranchiseMenu;
import com.beyond.Team3.bonbon.franchiseMenu.entity.FranchiseMenuId;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranchiseMenuRepository extends JpaRepository<FranchiseMenu, FranchiseMenuId> {
    boolean existsByFranchiseIdAndMenuId(Franchise franchise, Menu menu);

    List<Menu> findByFranchiseAndCategory(Long franchiseId, Long categoryId);
}

package com.beyond.Team3.bonbon.franchiseMenu.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;

import java.util.List;

public interface FranchiseMenuRepositoryCustom {
    List<Menu> findByFranchiseAndCategory(Long franchiseId, Long categoryId);

}

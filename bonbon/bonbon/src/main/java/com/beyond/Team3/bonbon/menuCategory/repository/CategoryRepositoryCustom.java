package com.beyond.Team3.bonbon.menuCategory.repository;

import com.beyond.Team3.bonbon.menuCategory.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    List<Category> findDistinctByHeadquarterId(Long headquarterId);

}

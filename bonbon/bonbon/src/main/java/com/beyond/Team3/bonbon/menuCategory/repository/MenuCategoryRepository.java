package com.beyond.Team3.bonbon.menuCategory.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menuCategory.entity.MenuCategory;
import com.beyond.Team3.bonbon.menuCategory.entity.MenuCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, MenuCategoryId>, MenuCategoryRepositoryCustom {
    List<MenuCategory> findByCategoryCategoryId(Long categoryId);

    void deleteByMenu(Menu menu);
}
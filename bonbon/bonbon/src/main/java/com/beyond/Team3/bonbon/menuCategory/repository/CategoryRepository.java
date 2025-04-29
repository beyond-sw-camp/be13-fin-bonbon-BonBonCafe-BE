package com.beyond.Team3.bonbon.menuCategory.repository;

import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long>, CategoryRepositoryCustom {
}

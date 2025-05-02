package com.beyond.Team3.bonbon.menuCategory.repository;

import com.beyond.Team3.bonbon.menu.entity.QMenu;
import com.beyond.Team3.bonbon.menuCategory.entity.Category;
import com.beyond.Team3.bonbon.menuCategory.entity.QCategory;
import com.beyond.Team3.bonbon.menuCategory.entity.QMenuCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Category> findDistinctByHeadquarterId(Long headquarterId) {
        QMenuCategory menuCategory = QMenuCategory.menuCategory;
        QMenu menu = QMenu.menu;
        QCategory category = new QCategory("category");

        return queryFactory
                .selectDistinct(menuCategory.category)
                .from(menuCategory)
                .join(menuCategory.menu, menu)
                .join(menuCategory.category, category)
                .where(menu.headquarter.headquarterId.eq(headquarterId))
                .fetch();
    }
}
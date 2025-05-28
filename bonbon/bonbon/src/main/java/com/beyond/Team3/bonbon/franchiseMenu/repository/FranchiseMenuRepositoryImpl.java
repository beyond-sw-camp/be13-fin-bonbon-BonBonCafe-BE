package com.beyond.Team3.bonbon.franchiseMenu.repository;

import com.beyond.Team3.bonbon.franchiseMenu.entity.QFranchiseMenu;
import com.beyond.Team3.bonbon.ingredient.entity.QIngredient;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.entity.QMenu;
import com.beyond.Team3.bonbon.menuCategory.entity.QMenuCategory;
import com.beyond.Team3.bonbon.menuDetail.entity.QMenuDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FranchiseMenuRepositoryImpl implements FranchiseMenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Menu> findByFranchiseAndCategory(Long franchiseId, Long categoryId) {
        QFranchiseMenu fm = QFranchiseMenu.franchiseMenu;
        QMenu menu = QMenu.menu;
        QMenuCategory mc1 = QMenuCategory.menuCategory;
        QMenuCategory mc2 = new QMenuCategory("mc2");
        QMenuDetail menuDetail = QMenuDetail.menuDetail;
        QIngredient ingredient = QIngredient.ingredient;

        List<Menu> menus = queryFactory
                .selectDistinct(menu)
                .from(fm)
                .join(fm.menuId, menu)
                .leftJoin(menu.categories, mc2).fetchJoin()
                .leftJoin(mc2.category).fetchJoin()
                .leftJoin(menu.details, menuDetail).fetchJoin()
                .leftJoin(menuDetail.ingredient, ingredient).fetchJoin()
                .where(
                        fm.franchiseId.franchiseId.eq(franchiseId)
                )
                .fetch();

        // Java 코드에서 필터링
        return menus.stream()
                .filter(m -> m.getCategories().stream()
                        .anyMatch(mc -> mc.getCategory().getCategoryId().equals(categoryId)))
                .toList();
    }
}
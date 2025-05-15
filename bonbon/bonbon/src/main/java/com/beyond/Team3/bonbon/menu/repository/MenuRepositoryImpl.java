package com.beyond.Team3.bonbon.menu.repository;

import com.beyond.Team3.bonbon.ingredient.entity.QIngredient;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.entity.QMenu;
import com.beyond.Team3.bonbon.menuCategory.entity.QCategory;
import com.beyond.Team3.bonbon.menuCategory.entity.QMenuCategory;
import com.beyond.Team3.bonbon.menuDetail.entity.QMenuDetail;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.beyond.Team3.bonbon.menuCategory.entity.QMenuCategory.menuCategory;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Menu> findAllMenu(Pageable pageable, Long headquarterId, String search) {
        QMenu menu = QMenu.menu;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(menu.headquarter.headquarterId.eq(headquarterId));

        if (search != null && !search.trim().isEmpty()) {
            builder.and(menu.name.containsIgnoreCase(search));
        }

        List<Menu> content = queryFactory
                .selectFrom(menu)
                .leftJoin(menu.categories, menuCategory).fetchJoin()
                .leftJoin(menuCategory.category).fetchJoin()
                .leftJoin(menu.details, QMenuDetail.menuDetail).fetchJoin()
                .leftJoin(QMenuDetail.menuDetail.ingredient).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        Long total = queryFactory
                .select(menu.count())
                .from(menu)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Menu> findMenusByCategoryAndHeadquarter(Long categoryId, Long headquarterId) {
        QMenu menu = QMenu.menu;
        QMenuCategory menuCategory = QMenuCategory.menuCategory;
        QMenuCategory menuCategory2 = new QMenuCategory("menuCategory2");
        QMenuDetail menuDetail = QMenuDetail.menuDetail;
        QIngredient ingredient = QIngredient.ingredient;
        QCategory category = QCategory.category;

        return queryFactory
                .selectDistinct(menu)
                .from(menuCategory)
                .join(menuCategory.menu, menu)
                .leftJoin(menu.categories, menuCategory2).fetchJoin()
                .leftJoin(menuCategory2.category, category).fetchJoin()
                .leftJoin(menu.details, menuDetail).fetchJoin()
                .leftJoin(menuDetail.ingredient, ingredient).fetchJoin()
                .where(
                        menuCategory.category.categoryId.eq(categoryId),
                        menu.headquarter.headquarterId.eq(headquarterId)
                )
                .fetch();
    }
}

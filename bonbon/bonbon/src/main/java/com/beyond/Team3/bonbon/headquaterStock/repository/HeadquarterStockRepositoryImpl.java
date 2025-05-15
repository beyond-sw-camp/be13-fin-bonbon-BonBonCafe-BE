package com.beyond.Team3.bonbon.headquaterStock.repository;

import com.beyond.Team3.bonbon.headquarter.entity.QHeadquarter;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.headquaterStock.entity.QHeadquarterStock;
import com.beyond.Team3.bonbon.ingredient.entity.QIngredient;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class HeadquarterStockRepositoryImpl implements HeadquarterStockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HeadquarterStock> getAllStock(Pageable pageable, Long headquarterId, String search) {
        QHeadquarterStock stock = QHeadquarterStock.headquarterStock;
        QIngredient ingredient = QIngredient.ingredient;
        QHeadquarter headquarter = QHeadquarter.headquarter;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(stock.headquarter.headquarterId.eq(headquarterId));

        if (search != null && !search.isBlank()) {
            builder.and(stock.ingredient.ingredientName.containsIgnoreCase(search));
        }

        List<HeadquarterStock> content = queryFactory
                .selectFrom(stock).distinct()
                .leftJoin(stock.ingredient, ingredient).fetchJoin()
                .leftJoin(stock.headquarter, headquarter).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(stock.count())
                .from(stock)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public List<HeadquarterStock> findByHeadquarterIdWithIngredient(Long headquarterId) {
        QHeadquarterStock stock = QHeadquarterStock.headquarterStock;
        QIngredient ingredient = QIngredient.ingredient;

        return queryFactory
                .selectFrom(stock)
                .leftJoin(stock.ingredient, ingredient).fetchJoin()
                .where(stock.headquarter.headquarterId.eq(headquarterId))
                .fetch();
    }

}

package com.beyond.Team3.bonbon.franchiseStock.repository;

import com.beyond.Team3.bonbon.franchise.entity.QFranchise;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.franchiseStock.entity.QFranchiseStock;
import com.beyond.Team3.bonbon.ingredient.entity.QIngredient;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class FranchiseStockRepositoryImpl implements FranchiseStockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FranchiseStock> getAllStock(Pageable pageable, Long franchiseId) {
        QFranchiseStock stock = QFranchiseStock.franchiseStock;
        QIngredient ingredient = QIngredient.ingredient;
        QFranchise franchise = QFranchise.franchise;

        List<FranchiseStock> content = queryFactory
                .selectFrom(stock)
                .leftJoin(stock.ingredientId, ingredient).fetchJoin()
                .leftJoin(stock.franchiseId, franchise).fetchJoin()
                .where(stock.franchiseId.franchiseId.eq(franchiseId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(stock.count())
                .from(stock)
                .where(stock.franchiseId.franchiseId.eq(franchiseId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }
}
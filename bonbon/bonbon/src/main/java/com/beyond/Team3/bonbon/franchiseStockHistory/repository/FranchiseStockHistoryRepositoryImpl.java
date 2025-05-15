package com.beyond.Team3.bonbon.franchiseStockHistory.repository;

import com.beyond.Team3.bonbon.franchise.entity.QFranchise;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.QFranchiseStockHistory;
import com.beyond.Team3.bonbon.ingredient.entity.QIngredient;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class FranchiseStockHistoryRepositoryImpl implements FranchiseStockHistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FranchiseStockHistory> getAllHistory(Pageable pageable, Long franchiseId) {
        QFranchiseStockHistory history = QFranchiseStockHistory.franchiseStockHistory;
        QIngredient ingredient = QIngredient.ingredient;
        QFranchise franchise = QFranchise.franchise;

        List<FranchiseStockHistory> content = queryFactory
                .selectFrom(history)
                .leftJoin(history.ingredientId, ingredient).fetchJoin()
                .leftJoin(history.franchiseId, franchise).fetchJoin()
                .where(history.franchiseId.franchiseId.eq(franchiseId))
                .orderBy(history.date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(history.count())
                .from(history)
                .where(history.franchiseId.franchiseId.eq(franchiseId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<FranchiseStockHistory> getAllFranchiseHistory(Pageable pageable, Long headquarterId) {
        QFranchiseStockHistory history = QFranchiseStockHistory.franchiseStockHistory;
        QIngredient ingredient = QIngredient.ingredient;
        QFranchise franchise = QFranchise.franchise;

        List<FranchiseStockHistory> content = queryFactory
                .selectFrom(history)
                .leftJoin(history.ingredientId, ingredient).fetchJoin()
                .leftJoin(history.franchiseId, franchise).fetchJoin()
                .where(franchise.headquarterId.headquarterId.eq(headquarterId))
                .orderBy(history.date.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(history.franchiseId, franchise)
                .where(franchise.headquarterId.headquarterId.eq(headquarterId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

}
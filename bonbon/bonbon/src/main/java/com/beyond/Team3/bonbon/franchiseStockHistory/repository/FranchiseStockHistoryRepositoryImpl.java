package com.beyond.Team3.bonbon.franchiseStockHistory.repository;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.franchise.entity.QFranchise;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.QFranchiseStockHistory;
import com.beyond.Team3.bonbon.ingredient.entity.QIngredient;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                .orderBy(history.historyId.desc())
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
    public Page<FranchiseStockHistory> getAllFranchiseHistory(Pageable pageable, Long headquarterId, HistoryStatus historyStatus) {
        QFranchiseStockHistory history = QFranchiseStockHistory.franchiseStockHistory;
        QIngredient ingredient = QIngredient.ingredient;
        QFranchise franchise = QFranchise.franchise;

        // where 조건 조합
        BooleanExpression condition = franchise.headquarterId.headquarterId.eq(headquarterId);
        if (historyStatus != null) {
            condition = condition.and(history.historyStatus.eq(historyStatus));
        }

        List<FranchiseStockHistory> content = queryFactory
                .selectFrom(history)
                .leftJoin(history.ingredientId, ingredient).fetchJoin()
                .leftJoin(history.franchiseId, franchise).fetchJoin()
                .where(condition)
                .orderBy(history.historyId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(history.count())
                .from(history)
                .leftJoin(history.franchiseId, franchise)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public Page<FranchiseStockHistory> findAllByFranchiseId_FranchiseIdAndHistoryStatus(
            Long franchiseId,
            HistoryStatus status,
            Pageable pageable) {

        QFranchiseStockHistory history = QFranchiseStockHistory.franchiseStockHistory;
        QIngredient ingredient = QIngredient.ingredient;
        QFranchise franchise = QFranchise.franchise;

        List<FranchiseStockHistory> content = queryFactory
                .selectFrom(history)
                .leftJoin(history.ingredientId, ingredient).fetchJoin()
                .leftJoin(history.franchiseId, franchise).fetchJoin()
                .where(
                        history.franchiseId.franchiseId.eq(franchiseId),
                        history.historyStatus.eq(status)
                )
                .orderBy(history.historyId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(history.count())
                .from(history)
                .where(
                        history.franchiseId.franchiseId.eq(franchiseId),
                        history.historyStatus.eq(status)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

}
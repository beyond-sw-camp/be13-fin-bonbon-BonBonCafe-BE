package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.sales.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.beyond.Team3.bonbon.menu.entity.QMenu.menu;
import static com.beyond.Team3.bonbon.sales.entity.QSalesDetail.salesDetail;

@Repository
public class SalesDetailRepositoryImpl implements SalesDetailRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SalesDetailRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<DailyTotalDto> findDailyTotalsByDate(LocalDate date) {

        return queryFactory
                .select(new QDailyTotalDto(
                        salesDetail.franchiseId.franchiseId,
                        salesDetail.amount.sum().intValue()))
                .from(salesDetail)
                .where(
                        salesDatedEq(date)
                )
                .groupBy(salesDetail.franchiseId.franchiseId)
                .fetch();
    }

    @Override
    public List<MenuRankingDto> findMenuRanking(Long franchiseId, LocalDate startDate, LocalDate endDate, int limit) {
        return queryFactory
                .select(new QMenuRankingDto(
                        menu.name,
                        salesDetail.productCount.sum().intValue(),
                        salesDetail.amount.sum().intValue()
                ))
                .from(salesDetail)
                .join(salesDetail.menu, menu)
                .where(
                        franchiseEq(franchiseId),
                        salesDateGoe(startDate),
                        salesDateLoe(endDate)
                )
                .groupBy(menu.name)
                .orderBy(salesDetail.productCount.sum().desc())
                .limit(limit)
                .fetch();
    }

    @Override
    public List<MenuRankingDto> findAllMenuRanking(LocalDate startDate, LocalDate endDate, int limit) {
        return queryFactory
                .select(new QMenuRankingDto(
                        menu.name,
                        salesDetail.productCount.sum().intValue(),
                        salesDetail.amount.sum().intValue()))
                .from(salesDetail)
                .join(salesDetail.menu, menu)
                .where(
                        salesDateGoe(startDate),
                        salesDateLoe(endDate)
                )
                .groupBy(menu.name)
                .orderBy(salesDetail.productCount.sum().desc())
                .limit(limit)
                .fetch();
    }

    // 헬퍼 메소드
    private BooleanExpression salesDatedEq(LocalDate date) {
        return date != null ? salesDetail.salesDetailId.salesDate.eq(date) : null;
    }

    private BooleanExpression franchiseEq(Long franchiseId) {
        return franchiseId != null ? salesDetail.franchiseId.franchiseId.eq(franchiseId) : null;
    }

    private BooleanExpression salesDateGoe(LocalDate startDate) {
        return startDate != null ? salesDetail.salesDetailId.salesDate.goe(startDate) : null;
    }

    private BooleanExpression salesDateLoe(LocalDate endDate) {
        return endDate != null ? salesDetail.salesDetailId.salesDate.loe(endDate) : null;
    }

}

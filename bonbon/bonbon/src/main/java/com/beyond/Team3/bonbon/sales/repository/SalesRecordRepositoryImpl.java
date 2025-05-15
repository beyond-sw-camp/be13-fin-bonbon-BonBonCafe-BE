package com.beyond.Team3.bonbon.sales.repository;

import com.beyond.Team3.bonbon.sales.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.beyond.Team3.bonbon.franchise.entity.QFranchise.franchise;
import static com.beyond.Team3.bonbon.sales.entity.QSalesRecord.salesRecord;

@Repository
public class SalesRecordRepositoryImpl implements SalesRecordRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public SalesRecordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<DailySalesDto> getDailySalesByPeriod(Long franchiseId, LocalDate startDate, LocalDate endDate) {

        return queryFactory
                .select(new QDailySalesDto(
                        salesRecord.salesDate,
                        salesRecord.salesAmount))
                .from(salesRecord)
                .where(
                        franchiseIdEq(franchiseId),
                        salesDateGoe(startDate),
                        salesDateLoe(endDate)
                )
                .orderBy(salesRecord.salesDate.asc())
                .fetch();
    }

    @Override
    public Page<SalesRankingDto> getFranchiseRanking(int regionCode, Integer year, Integer month, Pageable pageable) {
        List<SalesRankingDto> content = queryFactory
                .select(new QSalesRankingDto(
                        franchise.name,
                        salesRecord.salesAmount.sum().intValue()
                ))
                .from(salesRecord)
                .join(salesRecord.franchise, franchise)
                .where(
                        regionEq(regionCode),
                        yearEq(year),
                        monthEq(month)
                )
                .groupBy(franchise.franchiseId)
                .orderBy(salesRecord.salesAmount.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(franchise.franchiseId.countDistinct())
                .from(salesRecord)
                .join(salesRecord.franchise, franchise)
                .where(
                        regionEq(regionCode),
                        yearEq(year),
                        monthEq(month)
                );
//                .groupBy(franchise.franchiseId,franchise.name)
                // 리스트를 가져와서 그 리스트의 크기를 확인하는 방식 -> 성능 저하될 수 있음
//                .fetch()
//                .size();
                // null 반환 주의
//                .fetchOne();

//        return new PageImpl<>(content,pageable,totalCount);
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 헬퍼 메소드
    private BooleanExpression regionEq(Integer regionCode) {
        return regionCode != null ? salesRecord.franchise.regionCode.regionCode.eq(regionCode) : null;
    }

    private BooleanExpression yearEq(Integer year) {
        return year != null ? salesRecord.salesDate.year().eq(year) : null;
    }

    private BooleanExpression monthEq(Integer month) {
        return month != null ? salesRecord.salesDate.month().eq(month) : null;
    }

    private BooleanExpression franchiseIdEq(Long franchiseId) {
        return franchiseId != null ? salesRecord.franchise.franchiseId.eq(franchiseId) : null;
    }

    private BooleanExpression salesDateGoe(LocalDate startDate) {
        return startDate != null ? salesRecord.salesDate.goe(startDate) : null;
    }

    private BooleanExpression salesDateLoe(LocalDate endDate) {
        return endDate != null ? salesRecord.salesDate.loe(endDate) : null;
    }

}

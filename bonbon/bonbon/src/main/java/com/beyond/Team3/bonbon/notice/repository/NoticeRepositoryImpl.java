package com.beyond.Team3.bonbon.notice.repository;

import com.beyond.Team3.bonbon.common.enums.PostType;
import com.beyond.Team3.bonbon.notice.entity.Notice;
import com.beyond.Team3.bonbon.notice.entity.QNotice;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.beyond.Team3.bonbon.headquarter.entity.QHeadquarter.headquarter;

@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Notice> findAllNotice(Long headquarterId, Pageable pageable, String search, PostType postType) {
        QNotice notice = QNotice.notice;

        BooleanExpression condition = notice.headquarterId.headquarterId.eq(headquarterId);

        if (search != null && !search.isBlank()) {
            condition = condition.and(
                    notice.title.containsIgnoreCase(search)
                            .or(notice.content.containsIgnoreCase(search))
            );
        }

        if (postType != null) {
            condition = condition.and(notice.postType.eq(postType));
        }

        List<OrderSpecifier<?>> orders = getOrderSpecifiers(pageable, notice);

        List<Notice> content = queryFactory
                .selectFrom(notice)
                .leftJoin(notice.headquarterId, headquarter).fetchJoin()  // 중요
                .where(condition)
                .orderBy(orders.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(notice.count())
                .from(notice)
                .where(condition)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Pageable pageable, QNotice notice) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            if (order.getProperty().equals("noticeId")) {
                orders.add(new OrderSpecifier<>(
                        order.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                        notice.createdAt
                ));
            }
            // 필요한 정렬 항목 더 추가 가능
        }

        if (orders.isEmpty()) {
            // 기본 정렬: 최신순
            orders.add(notice.createdAt.desc());
        }

        return orders;
    }
}
package com.beyond.Team3.bonbon.menu.repository;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menu.entity.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Menu> findAllMenu(Pageable pageable, Long headquarterId, String search) {
        QMenu menu = QMenu.menu;

        List<Menu> content = queryFactory
                .selectFrom(menu)
                .where(
                        menu.headquarter.headquarterId.eq(headquarterId),
                        search != null ? menu.name.containsIgnoreCase(search) : null
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(menu.count())
                .from(menu)
                .where(
                        menu.headquarter.headquarterId.eq(headquarterId),
                        search != null ? menu.name.containsIgnoreCase(search) : null //대소문자 무시
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


}

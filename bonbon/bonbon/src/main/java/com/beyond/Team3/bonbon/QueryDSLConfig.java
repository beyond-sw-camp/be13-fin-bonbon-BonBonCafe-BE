package com.beyond.Team3.bonbon;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class QueryDSLConfig {

    private final EntityManager em;

    @Bean   // JPAQueryFactory를 미리 스프링 빈으로 등록
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
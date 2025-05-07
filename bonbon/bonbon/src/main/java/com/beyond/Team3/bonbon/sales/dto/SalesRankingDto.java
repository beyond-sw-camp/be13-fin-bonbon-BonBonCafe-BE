package com.beyond.Team3.bonbon.sales.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SalesRankingDto {
    private String franchiseName;
    private int totalSales;

    @QueryProjection
    public SalesRankingDto(String franchiseName, int totalSales) {
        this.franchiseName = franchiseName;
        this.totalSales = totalSales;
    }

}

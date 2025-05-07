package com.beyond.Team3.bonbon.sales.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuRankingDto {
    private String menuName;
    private int totalQuantity;
    private int totalAmount;

    @QueryProjection
    public MenuRankingDto(String menuName, int totalQuantity, int totalAmount) {
        this.menuName = menuName;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

}

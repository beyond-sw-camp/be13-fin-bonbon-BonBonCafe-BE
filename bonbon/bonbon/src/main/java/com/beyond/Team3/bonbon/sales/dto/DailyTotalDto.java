package com.beyond.Team3.bonbon.sales.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class DailyTotalDto {
    private long franchiseId;
    private int totalAmount;

    @QueryProjection
    public DailyTotalDto(long franchiseId, int totalAmount) {
        this.franchiseId = franchiseId;
        this.totalAmount = totalAmount;
    }
}

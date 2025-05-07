package com.beyond.Team3.bonbon.sales.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DailySalesDto {
    private LocalDate salesDate;

    private int totalAmount;

    @QueryProjection
    public DailySalesDto(LocalDate salesDate, int totalAmount) {
        this.salesDate = salesDate;
        this.totalAmount = totalAmount;
    }
}

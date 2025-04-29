package com.beyond.Team3.bonbon.sales.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesDto {
    private LocalDate salesDate;

    private int totalAmount;
}

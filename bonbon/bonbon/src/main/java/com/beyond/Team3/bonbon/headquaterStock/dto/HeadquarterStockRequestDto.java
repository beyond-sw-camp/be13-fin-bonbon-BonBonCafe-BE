package com.beyond.Team3.bonbon.headquaterStock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterStockRequestDto {

    private String ingredientName;
    private BigDecimal quantity;
}

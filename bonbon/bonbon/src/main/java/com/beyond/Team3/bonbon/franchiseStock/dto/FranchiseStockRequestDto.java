package com.beyond.Team3.bonbon.franchiseStock.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseStockRequestDto {

    private Long ingredientId;
    private BigDecimal quantity;
}

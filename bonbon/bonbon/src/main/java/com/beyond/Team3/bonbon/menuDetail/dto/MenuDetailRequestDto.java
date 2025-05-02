package com.beyond.Team3.bonbon.menuDetail.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MenuDetailRequestDto {
    private Long ingredientId;
    private BigDecimal quantity;
}

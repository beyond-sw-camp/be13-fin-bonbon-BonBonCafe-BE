package com.beyond.Team3.bonbon.menuDetail.dto;


import com.beyond.Team3.bonbon.menuDetail.entity.MenuDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@Getter
@AllArgsConstructor
public class MenuDetailResponseDto {
    private Long ingredientId;
    private String ingredientName;
    private String unit;
    private BigDecimal quantity;
    
    public static MenuDetailResponseDto from(MenuDetail detail) {
        return new MenuDetailResponseDto(
                detail.getIngredient().getIngredientId(),
                detail.getIngredient().getIngredientName(),
                detail.getIngredient().getUnit(),
                detail.getQuantity()
        );
    }
}
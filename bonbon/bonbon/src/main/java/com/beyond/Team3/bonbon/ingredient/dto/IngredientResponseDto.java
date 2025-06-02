package com.beyond.Team3.bonbon.ingredient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientResponseDto {
    private Long ingredientId;
    private String ingredientName;
    private String unit;
}

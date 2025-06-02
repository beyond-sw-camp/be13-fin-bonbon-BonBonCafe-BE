package com.beyond.Team3.bonbon.ingredient.dto;

import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IngredientRequestDto {
    private Long ingredientId;
    private String ingredientName;
    private String unit;


    public static IngredientRequestDto toEntity(Ingredient ingredient) {
        return new IngredientRequestDto(
                ingredient.getIngredientId(),
                ingredient.getIngredientName(),
                ingredient.getUnit());
    }
}
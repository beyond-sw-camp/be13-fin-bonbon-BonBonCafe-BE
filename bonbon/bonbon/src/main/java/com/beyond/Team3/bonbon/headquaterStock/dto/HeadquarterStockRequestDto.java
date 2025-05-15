package com.beyond.Team3.bonbon.headquaterStock.dto;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterStockRequestDto {

    private Long ingredientId;
    private BigDecimal quantity;

    public HeadquarterStock toEntity(Headquarter headquarter, Ingredient ingredient) {
        return HeadquarterStock.builder()
                .quantity(quantity)
                .headquarter(headquarter)
                .ingredient(ingredient)
                .build();
    }
}
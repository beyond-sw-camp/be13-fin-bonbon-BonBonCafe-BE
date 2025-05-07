package com.beyond.Team3.bonbon.headquaterStock.dto;

import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeadquarterStockResponseDto {

    private Long stockId;
    private Long headquarterId;
    private String headquarterName;

    private Long ingredientId;
    private String ingredientName;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal retailPrice;

    private BigDecimal quantity;

    public static HeadquarterStockResponseDto from(HeadquarterStock headquarterStock) {

        return new HeadquarterStockResponseDto(
                headquarterStock.getStockId(),
                headquarterStock.getHeadquarter().getHeadquarterId(),
                headquarterStock.getHeadquarter().getName(),
                headquarterStock.getIngredient().getIngredientId(),
                headquarterStock.getIngredient().getIngredientName(),
                headquarterStock.getIngredient().getUnit(),
                headquarterStock.getIngredient().getUnitPrice(),
                headquarterStock.getIngredient().getRetailPrice(),
                headquarterStock.getQuantity()
        );
    }

}

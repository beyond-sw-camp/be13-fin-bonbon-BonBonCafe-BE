package com.beyond.Team3.bonbon.franchiseStock.dto;

import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseStockResponseDto {

    private Long stockId;
    private Long franchiseId;
    private String franchiseName;

    private Long ingredientId;
    private String ingredientName;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal retailPrice;

    private BigDecimal quantity;

    public static FranchiseStockResponseDto from(FranchiseStock franchiseStock) {
        return new FranchiseStockResponseDto(
                franchiseStock.getStockId(),
                franchiseStock.getFranchiseId().getFranchiseId(),
                franchiseStock.getFranchiseId().getName(),
                franchiseStock.getIngredientId().getIngredientId(),
                franchiseStock.getIngredientId().getIngredientName(),
                franchiseStock.getIngredientId().getUnit(),
                franchiseStock.getIngredientId().getUnitPrice(),
                franchiseStock.getIngredientId().getRetailPrice(),
                franchiseStock.getQuantity()
        );
    }
}
package com.beyond.Team3.bonbon.franchiseStockHistory.dto;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseStockHistoryRequestDto {

    private Long ingredientId;
    private BigDecimal quantity;
    private HistoryStatus status;

    public FranchiseStockHistory toEntity(Franchise franchise, Ingredient ingredient) {
        return FranchiseStockHistory.builder()
                .franchiseId(franchise)
                .ingredientId(ingredient)
                .quantity(quantity)
                .date(LocalDate.now())
                .historyStatus(HistoryStatus.REQUESTED)
                .build();
    }
}

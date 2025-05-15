package com.beyond.Team3.bonbon.franchiseStockHistory.dto;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.franchiseStockHistory.entity.FranchiseStockHistory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseStockHistoryResponseDto {

    private Long franchiseId;
    private Long historyId;
    private Long ingredientId;
    private String ingredientName;
    private LocalDate date;
    private HistoryStatus historyStatus;
    private BigDecimal quantity;
    private String franchiseName;

    public static FranchiseStockHistoryResponseDto from(FranchiseStockHistory history) {
        return new FranchiseStockHistoryResponseDto(
                history.getFranchiseId().getFranchiseId(),
                history.getHistoryId(),
                history.getIngredientId().getIngredientId(),
                history.getIngredientId().getIngredientName(),
                history.getDate(),
                history.getHistoryStatus(),
                history.getQuantity(),
                history.getFranchiseId().getName()
        );
    }
}

package com.beyond.Team3.bonbon.franchiseStockHistory.entity;

import com.beyond.Team3.bonbon.common.enums.HistoryStatus;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchise_Stock_history")
public class FranchiseStockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredientId;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private HistoryStatus historyStatus;        // 재고 상태

    private BigDecimal quantity;        // 재고 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchiseId;

    public void updateHistory(BigDecimal quantity, HistoryStatus status) {
        this.quantity = quantity;
        this.date = LocalDate.now();
    }

    public void validateFranchise(Long franchiseId) {
        if (!this.franchiseId.getFranchiseId().equals(franchiseId)) {
            throw new IllegalArgumentException("해당 가맹점의 내역이 아닙니다.");
        }
    }

    public void updateHistoryByHeadquarter(BigDecimal quantity, HistoryStatus status) {
        this.quantity = quantity;
        this.date = LocalDate.now();
        this.historyStatus = status;
    }
}

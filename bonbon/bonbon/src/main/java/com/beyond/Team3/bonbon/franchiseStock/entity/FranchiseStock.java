package com.beyond.Team3.bonbon.franchiseStock.entity;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchiseStockHistory.dto.FranchiseStockHistoryRequestDto;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchise_stock")
public class FranchiseStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchiseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredientId;

    // 돈, 소수점 관련 내용은 BigDecimal을 사용
    //      -> 소수점 저장 타입인 float, double 타입은 소수점의 정밀도가 완벽하지 않아 오차가 생길 수 있음.
    private BigDecimal quantity;    // 가맹점 재고 수량

    public static FranchiseStock createFranchiseStock(Franchise franchise, Ingredient ingredient, FranchiseStockHistoryRequestDto dto) {
        return FranchiseStock.builder()
                .quantity(BigDecimal.ZERO)
                .franchiseId(franchise)
                .ingredientId(ingredient)
                .build();
    }

    public void updateStock(Ingredient ingredient, BigDecimal quantity) {
        this.ingredientId = ingredient;
        this.quantity = quantity;
    }

    public void addQuantity(BigDecimal quantity) {
        this.quantity = this.quantity.add(quantity);
    }

    public void subtractQuantity(BigDecimal quantity) {
        this.quantity = this.quantity.subtract(quantity);
    }
}

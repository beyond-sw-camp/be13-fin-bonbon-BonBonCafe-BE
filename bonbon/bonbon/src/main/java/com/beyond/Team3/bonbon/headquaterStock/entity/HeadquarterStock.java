package com.beyond.Team3.bonbon.headquaterStock.entity;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquaterStock.dto.HeadquarterStockRequestDto;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "headquarter_stock")
public class HeadquarterStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stockId;

    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public static HeadquarterStock createHeadquarterStock(Headquarter headquarter, Ingredient ingredient, HeadquarterStockRequestDto dto) {
        return HeadquarterStock.builder()
                .quantity(dto.getQuantity())
                .headquarter(headquarter)
                .ingredient(ingredient)
                .build();
    }

    public void updateStock(Ingredient ingredient, BigDecimal quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }
}

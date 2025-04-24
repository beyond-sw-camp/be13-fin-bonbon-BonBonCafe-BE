package com.beyond.Team3.bonbon.menuDetail.entity;

import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu_detail")
public class MenuDetail {

    @EmbeddedId
    private MenuDetailId id;

    @MapsId("menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menuId;

    @MapsId("ingredientId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredientId;

    private BigDecimal quantity;
}

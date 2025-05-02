package com.beyond.Team3.bonbon.menuDetail.entity;

import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
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
    private Menu menu;

    @MapsId("ingredientId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    private BigDecimal quantity;

    public MenuDetail(Menu menu, Ingredient ingredient, BigDecimal quantity) {
        this.menu = menu;
        this.ingredient = ingredient;
        this.quantity = quantity;

        this.id = new MenuDetailId(menu.getMenuId(), ingredient.getIngredientId());

    }
}

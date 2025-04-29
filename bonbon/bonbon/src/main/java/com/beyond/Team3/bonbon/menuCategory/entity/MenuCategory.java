package com.beyond.Team3.bonbon.menuCategory.entity;

import com.beyond.Team3.bonbon.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu_category")
public class MenuCategory {

    @EmbeddedId
    private MenuCategoryId id;

    @MapsId("menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public MenuCategory(Menu menu, Category category) {
        this.menu = menu;
        this.category = category;
        this.id = new MenuCategoryId(menu.getMenuId(), category.getCategoryId());
    }
}

package com.beyond.Team3.bonbon.menuCategory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<MenuCategory> menuCategories = new ArrayList<>();

    public static Category createCategory(String categoryName) {
        return new Category(categoryName);
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public void updateCategory(String categoryName) {
        this.categoryName = categoryName;
    }

}

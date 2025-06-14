package com.beyond.Team3.bonbon.ingredient.entity;

import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.menuDetail.entity.MenuDetail;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "unit")
    private String unit;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "retail_price")
    private BigDecimal retailPrice;

    @OneToMany(mappedBy = "ingredient")
    private List<MenuDetail> details = new ArrayList<>();

    @OneToMany(mappedBy = "ingredient")
    private List<HeadquarterStock> stocks = new ArrayList<>();
}

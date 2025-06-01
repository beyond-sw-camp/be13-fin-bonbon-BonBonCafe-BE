package com.beyond.Team3.bonbon.ingredient.repository;

import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByIngredientNameContainingIgnoreCase(String search);
}

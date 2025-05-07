package com.beyond.Team3.bonbon.ingredient.repository;

import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByIngredientName(String name);

}

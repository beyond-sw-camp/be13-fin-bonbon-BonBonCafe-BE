package com.beyond.Team3.bonbon.ingredient.controller;

import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "재료", description = "재료")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping
    public ResponseEntity<List<IngredientDto>> getAllIngredients() {
        List<Ingredient> ingredients = ingredientRepository.findAll();
        List<IngredientDto> response = ingredients.stream()
                .map(ing -> new IngredientDto(
                        ing.getIngredientId(),
                        ing.getIngredientName(),
                        ing.getUnit()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @Getter
    @AllArgsConstructor
    public static class IngredientDto {
        private Long ingredientId;
        private String ingredientName;
        private String unit;
    }
}
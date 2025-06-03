package com.beyond.Team3.bonbon.ingredient.controller;

import com.beyond.Team3.bonbon.ingredient.dto.IngredientResponseDto;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import com.beyond.Team3.bonbon.ingredient.repository.IngredientRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "재료", description = "재료")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping
    public ResponseEntity<List<IngredientResponseDto>> getAllIngredients(@RequestParam(required = false) String search) {
        List<Ingredient> ingredients;

        if (search != null && !search.trim().isEmpty()) {
            ingredients = ingredientRepository.findByIngredientNameContainingIgnoreCase(search);
        } else {
            ingredients = ingredientRepository.findAll();
        }

        List<IngredientResponseDto> response = ingredients.stream()
                .map(ing -> new IngredientResponseDto(
                        ing.getIngredientId(),
                        ing.getIngredientName(),
                        ing.getUnit()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }
}
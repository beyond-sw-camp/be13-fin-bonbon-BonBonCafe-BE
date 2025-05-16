package com.beyond.Team3.bonbon.headquaterStock.repository;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HeadquarterStockRepository extends JpaRepository<HeadquarterStock, Long>, HeadquarterStockRepositoryCustom {
    Page<HeadquarterStock> getAllStock(Pageable pageable, Long headquarterId, String search);

    boolean existsByHeadquarterAndIngredient(Headquarter headquarter, Ingredient ingredient);


    Optional<HeadquarterStock> findByHeadquarter_HeadquarterIdAndIngredient_IngredientId(Long headquarterId, Long ingredientId);

    List<HeadquarterStock> findByHeadquarterIdWithIngredient(Long headquarterId);
}

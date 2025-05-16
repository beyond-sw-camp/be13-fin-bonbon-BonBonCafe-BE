package com.beyond.Team3.bonbon.franchiseStock.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FranchiseStockRepository extends JpaRepository<FranchiseStock, Long>, FranchiseStockRepositoryCustom {

    Page<FranchiseStock> getAllStock(Pageable pageable, Long franchiseId);

    boolean existsByFranchiseIdAndIngredientId(Franchise franchise, Ingredient ingredient);

    Optional<FranchiseStock> findByFranchiseIdAndIngredientId(Franchise franchise, Ingredient ingredient);
}
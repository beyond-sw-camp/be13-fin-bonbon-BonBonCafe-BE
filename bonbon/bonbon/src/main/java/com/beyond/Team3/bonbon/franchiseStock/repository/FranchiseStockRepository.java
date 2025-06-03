package com.beyond.Team3.bonbon.franchiseStock.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchiseStock.entity.FranchiseStock;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FranchiseStockRepository extends JpaRepository<FranchiseStock, Long>, FranchiseStockRepositoryCustom {

    Page<FranchiseStock> getAllStock(Pageable pageable, Long franchiseId);

    boolean existsByFranchiseIdAndIngredientId(Franchise franchise, Ingredient ingredient);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fs FROM FranchiseStock fs WHERE fs.franchiseId = :franchise AND fs.ingredientId = :ingredient")
    Optional<FranchiseStock> findWithLockByFranchiseIdAndIngredientId(
            @Param("franchise") Franchise franchise,
            @Param("ingredient") Ingredient ingredient
    );
}
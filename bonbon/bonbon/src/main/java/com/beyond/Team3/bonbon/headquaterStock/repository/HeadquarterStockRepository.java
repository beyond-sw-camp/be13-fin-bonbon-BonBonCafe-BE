package com.beyond.Team3.bonbon.headquaterStock.repository;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import com.beyond.Team3.bonbon.ingredient.entity.Ingredient;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeadquarterStockRepository extends JpaRepository<HeadquarterStock, Long>, HeadquarterStockRepositoryCustom {
    Page<HeadquarterStock> getAllStock(Pageable pageable, Long headquarterId, String search);

    boolean existsByHeadquarterAndIngredient(Headquarter headquarter, Ingredient ingredient);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM HeadquarterStock h WHERE h.headquarter = :headquarter AND h.ingredient = :ingredient")
    Optional<HeadquarterStock> findWithLockByHeadquarterAndIngredient(Headquarter headquarter, Ingredient ingredient);

    Optional<HeadquarterStock> findByHeadquarter_HeadquarterIdAndIngredient_IngredientId(Long headquarterId, Long ingredientId);

    List<HeadquarterStock> findByHeadquarterIdWithIngredient(Long headquarterId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM HeadquarterStock s WHERE s.headquarter.headquarterId = :hqId AND s.ingredient.ingredientId = :ingredientId")
    Optional<HeadquarterStock> findByHeadquarterAndIngredientWithLock(
            @Param("hqId") Long headquarterId,
            @Param("ingredientId") Long ingredientId
    );
}

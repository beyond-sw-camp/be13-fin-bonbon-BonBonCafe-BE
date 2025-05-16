package com.beyond.Team3.bonbon.headquaterStock.repository;

import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HeadquarterStockRepositoryCustom {
    Page<HeadquarterStock> getAllStock(Pageable pageable, Long headquarterId, String search);

    List<HeadquarterStock> findByHeadquarterIdWithIngredient(Long headquarterId);

}

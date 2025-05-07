package com.beyond.Team3.bonbon.headquaterStock.repository;

import com.beyond.Team3.bonbon.headquaterStock.entity.HeadquarterStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HeadquarterStockRepositoryCustom {
    Page<HeadquarterStock> getAllStock(Pageable pageable, Long headquarterId);
}

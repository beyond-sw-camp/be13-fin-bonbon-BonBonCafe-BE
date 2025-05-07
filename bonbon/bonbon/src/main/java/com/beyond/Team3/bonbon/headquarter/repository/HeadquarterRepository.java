package com.beyond.Team3.bonbon.headquarter.repository;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeadquarterRepository extends JpaRepository<Headquarter, Long> {

    Headquarter findByHeadquarterId(Long headquarterId);
}

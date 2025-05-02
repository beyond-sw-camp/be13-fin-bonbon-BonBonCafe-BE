package com.beyond.Team3.bonbon.franchise.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

    Optional<Franchise> findByFranchiseId(Long franchiseId);
}

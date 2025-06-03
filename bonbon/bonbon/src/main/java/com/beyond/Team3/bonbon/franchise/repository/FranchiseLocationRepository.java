package com.beyond.Team3.bonbon.franchise.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.FranchiseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FranchiseLocationRepository extends JpaRepository<FranchiseLocation, Long> {

    boolean existsByFranchise(Franchise franchise);

    FranchiseLocation findByFranchise(Franchise franchise);

    List<FranchiseLocation> findByFranchise_NameContainingOrFranchise_RoadAddressContaining(String trim, String trim1);
}

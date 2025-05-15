package com.beyond.Team3.bonbon.franchise.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

    Optional<Franchise> findByFranchiseId(Long franchiseId);

    @Query("select f " +
            "from Franchise f " +
            "left JOIN  f.franchisee fe " +
            "where fe is null ")
    List<Franchise> findWithoutOwner();

    List<Franchise> findByHeadquarterId_HeadquarterId(Long headquarterId);


    List<Franchise> findByRegionCode_RegionCode(int regionCode);

    Franchise findByName(String name);
}

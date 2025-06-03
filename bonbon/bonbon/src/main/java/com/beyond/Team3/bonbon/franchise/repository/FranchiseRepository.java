package com.beyond.Team3.bonbon.franchise.repository;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

    Optional<Franchise> findByFranchiseId(Long franchiseId);

    @Query("select f " +
            "from Franchise f " +
            "left JOIN  f.franchisee fe " +
            "where fe is null and f.headquarterId =:headquarterId")
    Page<Franchise> findWithoutOwner(@Param("headquarterId") Headquarter headquarterId, Pageable pageable);

    List<Franchise> findByHeadquarterId_HeadquarterId(Long headquarterId);


    List<Franchise> findByRegionCode_RegionCode(int regionCode);

    Optional<Franchise> findByName(String name);

    @Query("select f " +
            "from Franchise f " +
            "where f.regionCode.regionCode = :regionCode and f.headquarterId = :headquarter")
    Page<Franchise> findFranchiseListInRegion(@Param("regionCode") int regionCode,
                                              @Param("headquarter") Headquarter headquarter,
                                              Pageable pageable);

    boolean existsByRoadAddress(String roadAddress);

    @Query("SELECT f FROM Franchise f " +
            "WHERE (:region IS NULL OR f.roadAddress LIKE %:region%) " +
            "AND (:district IS NULL OR f.roadAddress LIKE %:district%) " +
            "AND (:name IS NULL OR f.name LIKE %:name%)")
    Page<Franchise> searchFranchises(@Param("region") String region,
                                     @Param("district") String district,
                                     @Param("name") String name,
                                     Pageable pageable);

}

package com.beyond.Team3.bonbon.region.repository;

import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.region.entity.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Region findByRegionCode(int regionCode);

    Region findByRegionName(RegionName regionName);

//    Optional<Region> findByRegionCodeOptional(int regionCode);
}

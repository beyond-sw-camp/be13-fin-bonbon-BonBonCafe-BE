package com.beyond.Team3.bonbon.region.Service;

import com.beyond.Team3.bonbon.franchise.dto.FranchiseDto;
import com.beyond.Team3.bonbon.region.dto.RegionDto;

import java.security.Principal;
import java.util.List;

public interface RegionService {

    List<String> getMajors();
    List<RegionDto> getSubByMajor(String major);
    List<FranchiseDto> findByRegionCode(int regionCode);
}

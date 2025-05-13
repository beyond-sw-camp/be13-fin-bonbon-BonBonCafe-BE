package com.beyond.Team3.bonbon.region.Service;

import com.beyond.Team3.bonbon.franchise.dto.FranchiseDto;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.repository.FranchiseRepository;
import com.beyond.Team3.bonbon.region.dto.RegionDto;
import com.beyond.Team3.bonbon.region.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService{

    private final RegionRepository regionRepository;
    private final FranchiseRepository franchiseRepository;

    @Override
    public List<String> getMajors() {

        return regionRepository.findAll().stream()
                .map(region -> region.getRegionName().getDisplayName().split(" ")[0])
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<RegionDto> getSubByMajor(String major) {

        return regionRepository.findAll().stream()
                .filter(region -> region.getRegionName().getDisplayName().startsWith(major))
                .map(region -> new RegionDto(region.getRegionCode(),
                        region.getRegionName().getDisplayName()))
                .sorted(Comparator.comparing(RegionDto::getName))
                .collect(Collectors.toList());
    }

    @Override
    public List<FranchiseDto> findByRegionCode(int regionCode) {
        List<Franchise> list = franchiseRepository.findByRegionCode_RegionCode(regionCode);
        return list.stream()
                .map(franchise -> new FranchiseDto(franchise.getFranchiseId(), franchise.getName()))
                .collect(Collectors.toList());
    }

}

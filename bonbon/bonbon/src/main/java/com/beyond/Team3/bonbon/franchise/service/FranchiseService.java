package com.beyond.Team3.bonbon.franchise.service;

import com.beyond.Team3.bonbon.franchise.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.util.List;

public interface FranchiseService {
    Page<FranchiseResponseDto> findAll(int page, int size, String region, String district, String name);
    FranchiseResponseDto findByFranchiseId(Long franchiseId);
    void createFranchise(Principal principal, @Valid FranchiseRequestDto requestDto);
    void updateFranchiseInfo(Long franchiseId, @Valid FranchiseUpdateRequestDto requestDto, Principal principal);
    FranchiseSummaryDto franchiseSummary(Long franchiseId);
    void deleteFranchise(Long franchiseId, Principal principal);

    List<LocationResponseDto> findAllLocation();

    List<LocationResponseDto> getLocationsTest();


}

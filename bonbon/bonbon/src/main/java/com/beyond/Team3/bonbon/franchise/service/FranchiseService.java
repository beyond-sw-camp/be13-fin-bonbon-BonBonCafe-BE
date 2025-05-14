package com.beyond.Team3.bonbon.franchise.service;

import com.beyond.Team3.bonbon.franchise.controller.FranchiseSummaryDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseLocationDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchisePageResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseRequestDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseResponseDto;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

public interface FranchiseService {
    FranchisePageResponseDto findAll(int page, int size);
    FranchiseResponseDto findByFranchiseId(Long franchiseId);
    void createFranchise(Principal principal, @Valid FranchiseRequestDto requestDto);
    void updateFranchiseInfo(Long franchiseId, @Valid FranchiseUpdateRequestDto requestDto);
    List<FranchiseLocationDto> getFranchiseLocations();

    FranchiseSummaryDto findByFranchiseNam(String name);
}

package com.beyond.Team3.bonbon.franchise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FranchisePageResponseDto {
    private List<FranchiseResponseDto> franchises;
    private long total;
}

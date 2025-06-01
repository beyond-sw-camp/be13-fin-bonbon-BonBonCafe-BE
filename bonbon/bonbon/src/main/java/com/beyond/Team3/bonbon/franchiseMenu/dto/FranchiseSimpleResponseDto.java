package com.beyond.Team3.bonbon.franchiseMenu.dto;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FranchiseSimpleResponseDto {
    private Long franchiseId;
    private String franchiseName;
    private String roadAddress;
    private String detailAddress;

    public static FranchiseSimpleResponseDto from(Franchise franchise) {
        return new FranchiseSimpleResponseDto(
                franchise.getFranchiseId(),
                franchise.getName(),
                franchise.getRoadAddress(),
                franchise.getDetailAddress()
        );
    }
}
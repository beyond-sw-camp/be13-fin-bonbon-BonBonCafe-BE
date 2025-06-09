package com.beyond.Team3.bonbon.franchise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseSummaryDto {
    private Long franchiseId;
    private String franchiseTel;
    private String managerName;
    private String managerTel;
}

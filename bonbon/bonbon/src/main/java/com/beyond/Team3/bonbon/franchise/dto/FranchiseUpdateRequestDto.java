package com.beyond.Team3.bonbon.franchise.dto;

import com.beyond.Team3.bonbon.common.enums.FranchiseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseUpdateRequestDto {
    private String name;

    private String franchiseTel;

    private String franchiseImage;      // 매장 사진

    private int storeSize;      // 매장 크기

    private int seatingCapacity;    // 매장 내 좌석 수

    private boolean parkingAvailability;    // 주차 가능 여부

    private FranchiseStatus status;          // 운영 상태 - 폐점 / 휴점 / 정상 운영

    private String openHours;




}

package com.beyond.Team3.bonbon.franchise.dto;

import com.beyond.Team3.bonbon.common.enums.FranchiseStatus;
import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.region.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
public class FranchiseRequestDto {

    private String name;

    private RegionName regionName;

    private String franchiseTel;

    private String roadAddress;     // 도로명 주소

    private String detailAddress;       // 상세 주소

    private LocalDate openDate;     // 개점 일자

    private String franchiseImage;      // 매장 사진

    private int storeSize;      // 매장 크기

    private int seatingCapacity;    // 매장 내 좌석 수

    private boolean parkingAvailability;    // 주차 가능 여부

    private FranchiseStatus status;          // 운영 상태 - 폐점 / 휴점 / 정상 운영

    private String openHours;


    public Franchise toEntity(Headquarter headquarter, Region region) {
        return Franchise.builder()
                .name(name)
                .headquarterId(headquarter)
                .regionCode(region)
                .franchiseTel(franchiseTel)
                .roadAddress(roadAddress)
                .detailAddress(detailAddress)
                .openDate(openDate)
                .franchiseImage(franchiseImage)
                .storeSize(storeSize)
                .seatingCapacity(seatingCapacity)
                .parkingAvailability(parkingAvailability)
                .status(status)
                .openHours(openHours)
                .build();
    }





}

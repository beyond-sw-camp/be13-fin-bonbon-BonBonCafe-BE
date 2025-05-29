package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.common.enums.AccountStatus;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseFilterDto {

    private Long franchiseId;

    private String name;

    private String franchiseTel;

    private String roadAddress;     // 도로명 주소

    private AccountStatus status;


//    public FranchiseResponseDto(Franchise franchise, String managerName, RegionName regionName ) {
//        this.franchiseId = franchise.getFranchiseId();
//        this.managerName = managerName;
//        this.regionName = regionName != null ? regionName.name() : null;
////        this.regionCode = franchise.getRegionCode().getRegionCode();
//        this.headquarterId = franchise.getHeadquarterId().getHeadquarterId();
//        this.name = franchise.getName();
//        this.franchiseTel = franchise.getFranchiseTel();
//        this.roadAddress = franchise.getRoadAddress();
//        this.detailAddress = franchise.getDetailAddress();
//        this.openDate = franchise.getOpenDate();
//        this.franchiseImage = franchise.getFranchiseImage();
//        this.storeSize = franchise.getStoreSize();
//        this.seatingCapacity = franchise.getSeatingCapacity();
//        this.parkingAvailability = franchise.isParkingAvailability();
//        this.status = franchise.getStatus();
//        this.openHours = franchise.getOpenHours();
//    }

    public FranchiseFilterDto(Franchise franchise) {
        this.franchiseId = franchise.getFranchiseId();
        this.name = franchise.getName();
        this.franchiseTel = franchise.getFranchiseTel();
        this.roadAddress = franchise.getRoadAddress();
    }
}

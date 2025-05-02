package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FranchiseInfoDto {
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String status;

    private String regionName;

//    public FranchiseInfoDto(Franchise franchise) {
//        this.id = franchise.getFranchiseId();
//        this.name = franchise.getName();
//        this.address = franchise.getRoadAddress();
//        this.phone = franchise.getPhone();
//        this.status = franchise.getStatus().name(); // enum이면 .name() 또는 커스텀 변환
//        this.regionName = franchise.getRegion().getName(); // 연관된 Region 정보
//    }
}

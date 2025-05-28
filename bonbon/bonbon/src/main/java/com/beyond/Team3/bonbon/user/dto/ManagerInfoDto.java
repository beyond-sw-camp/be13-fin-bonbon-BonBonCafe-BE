package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.franchise.entity.Franchisee;
import com.beyond.Team3.bonbon.franchise.entity.Manager;
import com.beyond.Team3.bonbon.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerInfoDto extends UserInfo {

    private Long managerId;

    private RegionName region;

    private int regionCode;

    public ManagerInfoDto(User user) {
        super(user);
    }

    public ManagerInfoDto(Manager manager) {
        super(manager.getUserId());
        this.managerId = manager.getManagerId();
        if(manager.getRegionCode() != null){
            this.region = manager.getRegionCode().getRegionName();
            this.regionCode = manager.getRegionCode().getRegionCode();
        }
    }
}

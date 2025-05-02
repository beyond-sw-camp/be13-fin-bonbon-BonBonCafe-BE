package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.common.enums.RegionName;
import com.beyond.Team3.bonbon.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerInfoDto extends UserInfo {

    private RegionName region;

    public ManagerInfoDto(User user) {
        super(user);
    }
}

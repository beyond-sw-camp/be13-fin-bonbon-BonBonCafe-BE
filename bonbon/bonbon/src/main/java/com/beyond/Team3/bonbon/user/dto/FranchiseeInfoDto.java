package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FranchiseeInfoDto extends UserInfo {

    private Long franchiseId;

    public FranchiseeInfoDto(User user) {
        super(user);
    }
}

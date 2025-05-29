package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerModifyDto extends UserModifyDto{
    private Long regionCode;

}

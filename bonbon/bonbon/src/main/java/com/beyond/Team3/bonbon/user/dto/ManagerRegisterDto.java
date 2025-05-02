package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.region.entity.Region;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerRegisterDto extends UserRegisterDto {

    private Region regionCode;
}

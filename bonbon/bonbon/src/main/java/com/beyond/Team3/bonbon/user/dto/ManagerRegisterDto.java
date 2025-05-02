package com.beyond.Team3.bonbon.user.dto;

import com.beyond.Team3.bonbon.region.Region;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerRegisterDto extends UserRegisterDto {

    private Region regionCode;
}

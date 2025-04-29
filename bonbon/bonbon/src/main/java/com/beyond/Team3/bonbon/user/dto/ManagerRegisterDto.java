package com.beyond.Team3.bonbon.user.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerRegisterDto extends UserRegisterDto {

    private String region;
}

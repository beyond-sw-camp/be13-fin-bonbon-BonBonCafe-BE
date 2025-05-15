package com.beyond.Team3.bonbon.franchise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FranchiseLocationDto {
    private String name;
    private double latitude;
    private double longitude;


    public FranchiseLocationDto(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}

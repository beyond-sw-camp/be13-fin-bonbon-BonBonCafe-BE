package com.beyond.Team3.bonbon.franchise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LocationResponseDto {
    private Long franchiseId;
    private String name;
    private double latitude;
    private double longitude;
    private String franchiseImage;
    private String memo;


    public LocationResponseDto(Long franchiseId , String name, double latitude, double longitude, String franchiseImage, String memo) {
        this.franchiseId = franchiseId;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.franchiseImage = franchiseImage;
        this.memo = memo;
    }

}

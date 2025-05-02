package com.beyond.Team3.bonbon.region.entity;

import com.beyond.Team3.bonbon.common.enums.RegionName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int region_code;

    @Enumerated(EnumType.STRING)
    private RegionName region_name;
}

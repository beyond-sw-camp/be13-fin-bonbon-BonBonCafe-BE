package com.beyond.Team3.bonbon.region.entity;

import com.beyond.Team3.bonbon.common.enums.RegionName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@Entity
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_code")
    private int regionCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "region_name")
    private RegionName regionName;
}

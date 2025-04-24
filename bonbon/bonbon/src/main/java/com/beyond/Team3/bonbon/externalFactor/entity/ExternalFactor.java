package com.beyond.Team3.bonbon.externalFactor.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "external_factor")
public class ExternalFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long externalId;

    private String weather;     // 날씨

    private LocalDate date;     // 날짜

    private int temperature;    // 기온

    private String region;      // 지역

    private boolean isHoliday;  // 공휴일 여부

    private boolean isWeekend;  // 주말 여부

}

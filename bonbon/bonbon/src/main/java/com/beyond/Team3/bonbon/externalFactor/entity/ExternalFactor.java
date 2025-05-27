package com.beyond.Team3.bonbon.externalFactor.entity;


import jakarta.persistence.Column;
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
    @Column(name = "external_id")
    private Long externalId;

    @Column(name = "weather")
    private String weather;     // 날씨

    @Column(name = "date")
    private LocalDate date;     // 날짜

    @Column(name = "temperature")
    private int temperature;    // 기온

    @Column(name = "region")
    private String region;      // 지역

    @Column(name = "is_holiday")
    private boolean isHoliday;  // 공휴일 여부

    @Column(name = "is_weekend")
    private boolean isWeekend;  // 주말 여부

}

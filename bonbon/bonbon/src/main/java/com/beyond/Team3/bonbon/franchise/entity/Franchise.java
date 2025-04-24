package com.beyond.Team3.bonbon.franchise.entity;

import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchise")
public class Franchise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long franchiseId;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager managerId;

    @OneToOne
    @JoinColumn(name = "franchisee_id")
    private Franchisee franchiseeId;

    @ManyToOne
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarterId;

    private String name;

    private String region;

    private String franchiseTel;

    private String roadAddress;     // 도로명 주소

    private String detailAddress;       // 상세 주소

    private LocalDate openDate;     // 개점 일자

    private String franchiseImage;      // 매장 사진

    private int storeSize;      // 매장 크기

    private int seatingCapacity;    // 매장 내 좌석 수

    private boolean parkingAvailability;    // 주차 가능 여부

    private String status;          // 운영 상태 - 폐점 / 휴점 / 정상 운영

    private String openHours;       // 운영 시간
}

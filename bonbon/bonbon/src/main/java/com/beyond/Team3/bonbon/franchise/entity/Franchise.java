package com.beyond.Team3.bonbon.franchise.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.common.enums.FranchiseStatus;
import com.beyond.Team3.bonbon.franchise.dto.FranchiseUpdateRequestDto;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.region.entity.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchise")
public class Franchise extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "franchise_id")
    private Long franchiseId;

    @ManyToOne
    @JoinColumn(name = "region_code")
    private Region regionCode;

    @ManyToOne
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarterId;

    @Column(name = "name")
    private String name;

    @Column(name = "franchise_tel")
    private String franchiseTel;

    @Column(name = "road_address")
    private String roadAddress;     // 도로명 주소

    @Column(name = "detail_address")
    private String detailAddress;       // 상세 주소

    @Column(name = "open_date")
    private LocalDate openDate;     // 개점 일자

    @Column(name = "franchise_image")
    private String franchiseImage;      // 매장 사진

    @Column(name = "store_size")
    private int storeSize;      // 매장 크기

    @Column(name = "seating_capacity")
    private int seatingCapacity;    // 매장 내 좌석 수

    @Column(name = "parking_availability")
    private boolean parkingAvailability;    // 주차 가능 여부

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FranchiseStatus status;          // 운영 상태 - 폐점 / 휴점 / 정상 운영

    @Column(name = "open_hours")
    private String openHours;       // 운영 시간

    @OneToOne(mappedBy = "franchise")
    private Franchisee franchisee;

    public void update(FranchiseUpdateRequestDto requestDto) {
        this.name = requestDto.getName();
        this.franchiseTel = requestDto.getFranchiseTel();
        this.franchiseImage = requestDto.getFranchiseImage();
        this.storeSize = requestDto.getStoreSize();
        this.seatingCapacity = requestDto.getSeatingCapacity();
        this.parkingAvailability = requestDto.isParkingAvailability();
        this.openHours = requestDto.getOpenHours();
    }

    public void disconnectFranchisee() {
        if (this.franchisee != null) {
            this.franchisee.setFranchise(null);
            this.franchisee = null;
        }
    }
}

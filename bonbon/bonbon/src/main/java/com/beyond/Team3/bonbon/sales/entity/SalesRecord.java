package com.beyond.Team3.bonbon.sales.entity;

import com.beyond.Team3.bonbon.externalFactor.entity.ExternalFactor;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sales_record")
public class SalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchiseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_id")
    private ExternalFactor externalId;

    private int salesAmount = 0;    // 일 매출 총액
}

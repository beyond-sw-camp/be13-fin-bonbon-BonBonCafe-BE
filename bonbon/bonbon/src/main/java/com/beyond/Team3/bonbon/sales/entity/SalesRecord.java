package com.beyond.Team3.bonbon.sales.entity;

import com.beyond.Team3.bonbon.externalFactor.entity.ExternalFactor;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sales_record")
public class SalesRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_id")
    private Long salesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchise;

    // 외부요인 어떻게 적용할지?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_id")
    private ExternalFactor externalFactor;

    @Column(name = "sales_amount")
    private int salesAmount = 0;    // 일 매출 총액

    @Column(name = "sales_date")
    private LocalDate salesDate;

    // 가맹점 등록 시 생성하도록
//    public static SalesRecord createSalesRecord(Franchise franchise) {
//        return SalesRecord.builder()
//                .franchise(franchise)
//                .build();
//    }

    public void updateSalesAmount(int salesAmount) {
        this.salesAmount = salesAmount;
    }
}

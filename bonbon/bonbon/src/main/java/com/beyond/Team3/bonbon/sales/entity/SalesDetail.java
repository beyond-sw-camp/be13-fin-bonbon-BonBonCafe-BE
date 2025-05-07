package com.beyond.Team3.bonbon.sales.entity;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sales_detail")
public class SalesDetail {

    @EmbeddedId
    private SalesDetailId salesDetailId;

    @MapsId("franchiseId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchiseId;

    @MapsId("menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int productCount = 0;       // 판매 수량

    private int amount = 0;         // 메뉴 판매 금액

    public LocalDate getSalesDate() {
        return this.salesDetailId.getSalesDate();
    }

    // 더미데이터로 우리가 직접 넣는 형식이라 필요는 없을 듯함
//    public static SalesDetail createSalesDetail(SalesRecord salesRecord, Menu menu, int productCount) {
//        return SalesDetail.builder()
//                .salesRecord(salesRecord)
//                .menu(menu)
//                .productCount(productCount)
//                .amount(menu.getPrice() * productCount)
//                .salesDate(LocalDate.now())
//                .build();
//    }
}




















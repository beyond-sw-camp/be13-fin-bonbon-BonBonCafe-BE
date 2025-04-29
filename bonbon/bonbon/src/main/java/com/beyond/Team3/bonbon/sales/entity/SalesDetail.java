package com.beyond.Team3.bonbon.sales.entity;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    private SalesRecord salesRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private int productCount = 0;       // 판매 수량

    private int amount = 0;         // 메뉴 판매 금액

    private LocalDate salesDate;

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




















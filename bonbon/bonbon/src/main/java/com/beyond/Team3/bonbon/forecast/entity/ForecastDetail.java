package com.beyond.Team3.bonbon.forecast.entity;


import com.beyond.Team3.bonbon.menu.entity.Menu;
import jakarta.persistence.Column;
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
@Table(name = "forecast_detail")
public class ForecastDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "forecast_id")
    private SalesForecast forecastId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menuId;

    @Column(name = "product_count", nullable = false)
    private int productCount = 0;       // 예상 판매량

    @Column(name = "amount", nullable = false)
    private int amount = 0;             // 예상 판매액
}

package com.beyond.Team3.bonbon.menu.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "menu")
public class Menu extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarterId;

    private String menuImage;

    private String name;

    private String decription;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private MenuStatus status = MenuStatus.ACTIVE;

}

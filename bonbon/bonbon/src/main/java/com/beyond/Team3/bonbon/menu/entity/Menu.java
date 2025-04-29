package com.beyond.Team3.bonbon.menu.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
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
import lombok.*;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "menu")
@Getter
public class Menu extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "headquarter_id")
    private Headquarter headquarter;

    @Column(name = "menu_image", nullable = false) //ProductThumbnail 클래스 생성하기
    private String menuImage;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int price;

    @Enumerated(EnumType.STRING)
    private MenuStatus status = MenuStatus.ACTIVE;

    public static Menu createMenu(MenuRequestDto dto, Headquarter headquarter) {
        return Menu.builder()
                .headquarter(headquarter)
                .menuImage(dto.getImage())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();
    }

    public void updateMenu(MenuRequestDto dto) {
        this.menuImage = dto.getImage();
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.price = dto.getPrice();
        this.status = dto.getStatus();
    }

    public boolean hasSameHeadquarter(Long headquarterId) {
        return this.headquarter != null && this.headquarter.getHeadquarterId().equals(headquarterId);
    }
}

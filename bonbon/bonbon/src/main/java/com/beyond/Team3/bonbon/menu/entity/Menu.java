package com.beyond.Team3.bonbon.menu.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.menu.dto.MenuRequestDto;
import com.beyond.Team3.bonbon.menuCategory.entity.MenuCategory;
import com.beyond.Team3.bonbon.menuDetail.entity.MenuDetail;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
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
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    @Builder.Default
    @OneToMany(mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MenuCategory> categories = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "menu", orphanRemoval = true, cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<MenuDetail> details = new HashSet<>();

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

    public void addCategory(MenuCategory menuCategory) {
        this.categories.add(menuCategory);
    }

    public void addDetail(MenuDetail detail) {
        this.details.add(detail);
    }
}

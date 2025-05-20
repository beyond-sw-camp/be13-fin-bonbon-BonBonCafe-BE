package com.beyond.Team3.bonbon.franchiseMenu.entity;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "franchise_menu")
public class FranchiseMenu {

    @EmbeddedId
    private FranchiseMenuId id;

    @MapsId("franchiseId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "franchise_id")
    private Franchise franchiseId;

    @MapsId("menuId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menuId;

    public static FranchiseMenu create(Franchise franchise, Menu menu) {
        return new FranchiseMenu(
                new FranchiseMenuId(franchise.getFranchiseId(), menu.getMenuId()),
                franchise,
                menu
        );
    }
}

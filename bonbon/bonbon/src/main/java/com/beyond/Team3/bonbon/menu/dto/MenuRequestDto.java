package com.beyond.Team3.bonbon.menu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.headquarter.entity.Headquarter;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menuDetail.dto.MenuDetailRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MenuRequestDto {

    private String image;

    private String name;

    private String description;

    private int price;

    private MenuStatus status;

    private List<Long> categoryIds;

    private List<MenuDetailRequestDto> menuDetails;

    public Menu toEntity(Headquarter headquarter) {
        return Menu.builder()
                .headquarter(headquarter)
                .menuImage(this.image)
                .name(this.name)
                .description(this.description)
                .price(this.price)
                .status(this.status)
                .build();
    }
}

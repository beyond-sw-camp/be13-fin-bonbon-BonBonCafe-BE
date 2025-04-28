package com.beyond.Team3.bonbon.menu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class MenuRequestDto {

    private String image;

    private String name;

    private String description;

    private int price;

    private MenuStatus status;
}

package com.beyond.Team3.bonbon.menu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
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

}

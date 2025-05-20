package com.beyond.Team3.bonbon.franchiseMenu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuDetail.dto.MenuDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class FranchiseMenuResponseDto {

    private Long menuId;
    private String name;
    private String description;
    private int price;
    private String image;
    private MenuStatus status;
    private List<CategoryResponseDto> categories;
    private List<MenuDetailResponseDto> menuDetails;

    public static FranchiseMenuResponseDto from(Menu menu) {
        return FranchiseMenuResponseDto.builder()
                .menuId(menu.getMenuId())
                .name(menu.getName())
                .description(menu.getDescription())
                .price(menu.getPrice())
                .image(menu.getMenuImage())
                .status(menu.getStatus())
                .categories(menu.getCategories().stream()
                        .map(c -> CategoryResponseDto.from(c.getCategory()))
                        .distinct()
                        .toList())
                .menuDetails(menu.getDetails().stream()
                        .map(MenuDetailResponseDto::from)
                        .toList())
                .build();
    }
}
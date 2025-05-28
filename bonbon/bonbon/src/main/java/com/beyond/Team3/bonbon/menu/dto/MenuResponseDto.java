package com.beyond.Team3.bonbon.menu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import com.beyond.Team3.bonbon.menuCategory.dto.CategoryResponseDto;
import com.beyond.Team3.bonbon.menuDetail.dto.MenuDetailResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDto {

    private Long menuId;
    private Long headquarterId;
    private String image;
    private String name;
    private String description;
    private int price;
    private MenuStatus status;
    private LocalDateTime createTime;
    private LocalDateTime modifyAt;
    private List<CategoryResponseDto> categories;
    private List<MenuDetailResponseDto> menuDetails;

    public static MenuResponseDto from(Menu menu) {
        List<CategoryResponseDto> categories = menu.getCategories().stream()
                .map(mc -> CategoryResponseDto.from(mc.getCategory()))
                .distinct()
                .toList();

        List<MenuDetailResponseDto> detailDtos = menu.getDetails().stream()
                .map(MenuDetailResponseDto::from)
                .sorted(Comparator.comparing(MenuDetailResponseDto::getIngredientId)) // ← 정렬 추가
                .toList();

        return new MenuResponseDto(
                menu.getMenuId(),
                menu.getHeadquarter().getHeadquarterId(),
                menu.getMenuImage(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.getStatus(),
                menu.getCreatedAt(),
                menu.getModifiedAt(),
                categories,
                detailDtos
        );
    }
}

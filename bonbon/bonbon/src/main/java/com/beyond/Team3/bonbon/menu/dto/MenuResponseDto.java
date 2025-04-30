package com.beyond.Team3.bonbon.menu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<Long> categoryIds;

    public static MenuResponseDto from(Menu menu) {
        List<Long> categoryIds = menu.getCategories().stream()
                .map(mc -> mc.getCategory().getCategoryId())
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
                menu.getCategories() != null ? categoryIds : new ArrayList<>()
        );
    }
}

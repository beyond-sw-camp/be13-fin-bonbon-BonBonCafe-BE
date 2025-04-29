package com.beyond.Team3.bonbon.menu.dto;

import com.beyond.Team3.bonbon.common.enums.MenuStatus;
import com.beyond.Team3.bonbon.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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

    public static MenuResponseDto menuResponseDto(Menu menu) {
        return new MenuResponseDto(
                menu.getMenuId(),
                menu.getHeadquarter().getHeadquarterId(),
                menu.getMenuImage(),
                menu.getName(),
                menu.getDescription(),
                menu.getPrice(),
                menu.getStatus(),
                menu.getCreatedAt(),
                menu.getModifiedAt()
        );
    }
}

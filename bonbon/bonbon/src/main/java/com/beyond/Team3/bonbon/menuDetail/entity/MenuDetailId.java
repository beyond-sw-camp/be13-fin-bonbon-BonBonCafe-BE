package com.beyond.Team3.bonbon.menuDetail.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MenuDetailId implements Serializable {

    private Long menuId;
    private Long ingredientId;
}

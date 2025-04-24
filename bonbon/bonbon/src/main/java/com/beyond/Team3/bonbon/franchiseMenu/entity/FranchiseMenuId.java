package com.beyond.Team3.bonbon.franchiseMenu.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FranchiseMenuId implements Serializable {

    private Long franchiseId;
    private Long menuId;
}

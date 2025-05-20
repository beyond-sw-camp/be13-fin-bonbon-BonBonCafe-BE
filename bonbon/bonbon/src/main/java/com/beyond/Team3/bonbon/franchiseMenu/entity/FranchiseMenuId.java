package com.beyond.Team3.bonbon.franchiseMenu.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FranchiseMenuId implements Serializable {

    private Long franchiseId;
    private Long menuId;
}

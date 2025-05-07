package com.beyond.Team3.bonbon.sales.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SalesDetailId implements Serializable {

    private Long franchiseId;
    private Long menuId;
    private LocalDate salesDate;

}

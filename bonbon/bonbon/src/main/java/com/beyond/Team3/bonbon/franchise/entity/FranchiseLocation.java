package com.beyond.Team3.bonbon.franchise.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long franchiseLocationId;

    @OneToOne
    @JoinColumn(name = "franchise_id", referencedColumnName = "franchise_id")
    private Franchise franchise;

    private Double latitude;
    private Double longitude;

}

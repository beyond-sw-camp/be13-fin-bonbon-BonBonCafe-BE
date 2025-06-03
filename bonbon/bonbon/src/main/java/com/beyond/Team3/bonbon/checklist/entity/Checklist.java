package com.beyond.Team3.bonbon.checklist.entity;

import com.beyond.Team3.bonbon.common.base.EntityDate;
import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "checklist")
public class Checklist extends EntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "franchise_id")
    private Franchise franchiseId;

    @Column(name = "checklist_type")
    private String checklistType;

    @Column(name = "next_check_date")
    private LocalDate nextCheckDate;    // 다음 점검일

    @Column(columnDefinition = "TEXT", name = "notes")
    private String notes;   // 점검 메모

    @Column(name = "photo")
    private String photo;

}

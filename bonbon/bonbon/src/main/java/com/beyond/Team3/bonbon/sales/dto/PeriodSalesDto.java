package com.beyond.Team3.bonbon.sales.dto;

import com.beyond.Team3.bonbon.franchise.entity.Franchise;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class PeriodSalesDto {
    private Long franchiseId;
    private LocalDate startDate;
    private LocalDate endDate;

}

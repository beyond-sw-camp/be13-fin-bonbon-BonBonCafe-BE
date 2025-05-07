package com.beyond.Team3.bonbon.Python.dto;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ForecastResultDto {
    private List<DailySalesDto> history;
    private List<ForecastResponseDto> forecast;
}

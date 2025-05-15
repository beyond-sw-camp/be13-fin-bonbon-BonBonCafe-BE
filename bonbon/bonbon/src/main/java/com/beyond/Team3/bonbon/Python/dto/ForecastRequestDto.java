package com.beyond.Team3.bonbon.Python.dto;

import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class ForecastRequestDto {
    private List<DailySalesDto> history;
    private int periods;

}

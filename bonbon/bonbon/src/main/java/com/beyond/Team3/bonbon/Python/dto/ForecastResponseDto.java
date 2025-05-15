package com.beyond.Team3.bonbon.Python.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ForecastResponseDto {

    private LocalDate ds; // 예측 날짜

    private Double yhat; // 예측 값

    @JsonProperty("yhat_lower")
    private Double yhatLower;

    @JsonProperty("yhat_upper")
    private Double yhatUpper;

}

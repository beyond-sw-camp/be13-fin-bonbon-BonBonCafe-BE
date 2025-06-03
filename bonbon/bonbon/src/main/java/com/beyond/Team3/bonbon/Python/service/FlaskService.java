package com.beyond.Team3.bonbon.Python.service;

import com.beyond.Team3.bonbon.Python.dto.ForecastRequestDto;
import com.beyond.Team3.bonbon.Python.dto.ForecastResponseDto;
import com.beyond.Team3.bonbon.common.enums.Role;
import com.beyond.Team3.bonbon.handler.exception.UserException;
import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import com.beyond.Team3.bonbon.sales.dto.DailySalesDto;
import com.beyond.Team3.bonbon.user.entity.User;
import com.beyond.Team3.bonbon.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FlaskService {

    //데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

//    private final String GLOBAL_URL = "http://127.0.0.1:8082/forecast/global";
//    private final String FRANCHISE_URL = "http://127.0.0.1:8082/forecast/franchise/%d";
    private final String FORECAST_URL = "http://127.0.0.1:8082/forecast";
    // 전체 가맹점 예측
    public List<ForecastResponseDto> getGlobalForecast(List<DailySalesDto> history, int periods)
            throws JsonProcessingException {

        // 헤더 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디 객체 생성
        ForecastRequestDto requestDto = new ForecastRequestDto(history, periods);
        // 자바 객체 -> JSON 문자열로 변환
        String body = objectMapper.writeValueAsString(requestDto);

        HttpEntity<String> entity = new HttpEntity<>(body,headers);

        ForecastResponseDto[] arr =
                restTemplate.postForObject(FORECAST_URL, entity, ForecastResponseDto[].class);

        return Arrays.asList(arr);
    }

    public List<ForecastResponseDto> getFranchiseForecast(Long franchiseId, List<DailySalesDto> history, int periods)
            throws JsonProcessingException {

        // 헤더 객체 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디 객체 생성
        ForecastRequestDto requestDto = new ForecastRequestDto(history, periods);

        Map<String,Object> wrapper = new HashMap<>();
        wrapper.put("franchiseId", franchiseId);
        wrapper.put("history", requestDto.getHistory());
        wrapper.put("periods", requestDto.getPeriods());

        String body = objectMapper.writeValueAsString(requestDto);
        HttpEntity<String> entity = new HttpEntity<>(body,headers);

        ForecastResponseDto[] arr =
                restTemplate.postForObject(FORECAST_URL, entity, ForecastResponseDto[].class);

        return Arrays.asList(arr);
    }
}
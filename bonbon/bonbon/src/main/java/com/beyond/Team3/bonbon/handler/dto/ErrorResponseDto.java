package com.beyond.Team3.bonbon.handler.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ErrorResponseDto {

    private int code;
    private String status;
    private String message;

}

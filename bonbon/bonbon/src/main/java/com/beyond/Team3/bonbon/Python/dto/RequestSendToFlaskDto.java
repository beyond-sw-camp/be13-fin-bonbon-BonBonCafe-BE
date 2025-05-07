package com.beyond.Team3.bonbon.Python.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RequestSendToFlaskDto {
    private final String nickname;
    private final String fileId;
}
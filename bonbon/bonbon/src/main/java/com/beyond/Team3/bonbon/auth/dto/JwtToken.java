package com.beyond.Team3.bonbon.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Data
@AllArgsConstructor
public class JwtToken {

    // 클라이언트에 보낼 토큰
    private String accessToken;     // accessToken
    private String refreshToken;    // refreshToken
}

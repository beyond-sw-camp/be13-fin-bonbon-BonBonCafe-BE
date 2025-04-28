package com.beyond.Team3.bonbon.handler.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS("이메일 혹은 비밀번호가 틀립니다.", HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다.", HttpStatus.CONFLICT),
    PASSWORD_NOT_MATCH("비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),;

    private final String message;
    private final HttpStatus status;
}

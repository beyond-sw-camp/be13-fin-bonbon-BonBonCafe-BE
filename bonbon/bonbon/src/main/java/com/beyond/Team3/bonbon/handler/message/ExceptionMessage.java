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
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),

    INVALID_PAGE_PARAMETER("유효한 값이 아닙니다.",  HttpStatus.BAD_REQUEST),

    FRANCHISE_NOT_FOUND("해당 가맹점을 찾을 수 없습니다.", HttpStatus.NOT_FOUND ),
    NO_SALES_RECORDS("해당 기간에 매출 기록이 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_DATE_RANGE("시작일은 종료일보다 이전이거나 같아야 합니다.",HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCOUNT_ACCESS("계정에 대한 수정 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_USER_ROLE("관리자만 접근 가능합니다.", HttpStatus.BAD_REQUEST),

    MANAGER_NOT_FOUND("담당 매니저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_FRANCHISE_MODIFY("가맹점을 수정할 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_FRANCHISE_DELETE("가맹점을 삭제할 권한이 없습니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED_FRANCHISE_CREATE("가맹점 등록 권한이 없습니다.", HttpStatus.FORBIDDEN),
//    TEMP_CLOSE_INVALID_STATUS("운영 중인 상태에서만 임시 휴점이 가능합니다.", HttpStatus.BAD_REQUEST),
    PERM_CLOSE_INVALID_STATUS("운영 중인 가맹점만 영구 폐점할 수 있습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_PERMANENT_CLOSED("이미 영구 폐점된 가맹점입니다.", HttpStatus.BAD_REQUEST),
    HEADQUARTER_NOT_FOUND("매니저가 본사에 소속되어 있지 않습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_REGISTERED_ADDRESS("이미 동일한 위치에 가맹점이 등록되어 있습니다.", HttpStatus.CONFLICT),
    KAKAO_API_FAILED("카카오 지도 API 호출 중 문제가 발생했습니다.", HttpStatus.BAD_GATEWAY),

    PASSWORD_CONFIRM_NOT_MATCH("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),;

    private final String message;
    private final HttpStatus status;
}

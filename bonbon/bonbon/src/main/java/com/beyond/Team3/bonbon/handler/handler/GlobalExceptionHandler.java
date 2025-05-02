package com.beyond.Team3.bonbon.handler.handler;



import com.beyond.Team3.bonbon.handler.dto.ErrorResponseDto;
import com.beyond.Team3.bonbon.handler.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // BaseException 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponseDto> handleException(BaseException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(
                        e.getStatus().value(),
                        e.getType(),
                        e.getMessage()),
                    e.getStatus()
                );
    }

    // valid 관련 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleException(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors
                    .append(fieldError.getField())
                    .append("(")
                    .append(fieldError.getDefaultMessage())
                    .append("), ");
        }

        errors.replace(errors.lastIndexOf(","), errors.length(), "");

        return new ResponseEntity<>(
                new ErrorResponseDto(
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.name(),
                        errors.toString()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    // 그 외 전체 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception e) {

        log.error("Global Exception : {}", e.getMessage());

        return new ResponseEntity<>(
                new ErrorResponseDto(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        e.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}

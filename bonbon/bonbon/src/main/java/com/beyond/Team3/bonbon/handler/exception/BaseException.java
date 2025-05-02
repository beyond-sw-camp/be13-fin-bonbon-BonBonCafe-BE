package com.beyond.Team3.bonbon.handler.exception;

import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = 4148152660577750333L;

    private final String type;
    private final HttpStatus status;

    protected BaseException(ExceptionMessage message) {
        super(message.getMessage());
        this.type = message.name();
        this.status = message.getStatus();
    }
}

package com.beyond.Team3.bonbon.handler.exception;

import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class FranchiseException extends BaseException {

    @Serial
    private static final long serialVersionUID = 7863465502058779903L;

    public FranchiseException(ExceptionMessage message) {
        super(message);
    }
}

package com.beyond.Team3.bonbon.handler.exception;

import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class FranchiseException extends BaseException{

    @Serial
    private static final long serialVersionUID = -8539352692637830207L;

    public FranchiseException(ExceptionMessage message) {
        super(message);
    }

}

package com.beyond.Team3.bonbon.handler.exception;

import com.beyond.Team3.bonbon.handler.message.ExceptionMessage;
import lombok.Getter;

import java.io.Serial;

@Getter
public class PageException extends BaseException {

    @Serial
    private static final long serialVersionUID = 30899073249472084L;

    public PageException(ExceptionMessage message) {
      super(message);
    }
}

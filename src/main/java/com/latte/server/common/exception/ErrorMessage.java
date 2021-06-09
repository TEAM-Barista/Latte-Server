package com.latte.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by Minky on 2021-06-09
 */
@Getter
public class ErrorMessage {
    private int code;
    private String message;
    private HttpStatus httpStatus;

    public ErrorMessage(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

package com.latte.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by Minky on 2021-06-09
 */
@Getter
public class CustomException extends RuntimeException {
    private HttpStatus status;
    private String message;

    public CustomException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

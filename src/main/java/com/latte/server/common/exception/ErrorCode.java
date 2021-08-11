package com.latte.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by Minky on 2021-08-11
 */

@Getter
public enum ErrorCode {
    INVALID_TOKEN(401, "Token is not Invalid, Please Check tour token.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(401, "Token is Expired, Please Refreshing token.", HttpStatus.UNAUTHORIZED),
    INVALID_EMAIL_OR_PASSWORD(401, "Email or Password, Please check your email or password.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_EMAIL(401, "Email is not found, Please check your email.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_PASSWORD(401, "Password is not found, Please check your Password.", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

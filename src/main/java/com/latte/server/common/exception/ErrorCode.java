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
    NOT_FOUND_PASSWORD(401, "Password is not found, Please check your Password.", HttpStatus.UNAUTHORIZED),
    NOT_FOUND_CATEGORY(404, "Cannot find category, Please check category id.", HttpStatus.NOT_FOUND),
    NOT_FOUND_USER_CATEGORY(404, "Cannot find user category, Please check your path variable.", HttpStatus.NOT_FOUND),
    FCM_NOT_ALLOWED(500, "Firebase Error. Please contact Latte Server Team", HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_FOUND_USER(404, "[ERROR] No such User", HttpStatus.NOT_FOUND),
    NOT_FOUND_INTERVIEW(404, "[ERROR] No such Interview", HttpStatus.NOT_FOUND),
    NOT_FOUND_TEXT(404, "[ERROR] Do not contain text", HttpStatus.NOT_FOUND),
    NOT_FOUND_POST(404, "[ERROR] No such Post", HttpStatus.NOT_FOUND),
    NOT_FOUND_REPLY(404, "[ERROR] No such Reply", HttpStatus.NOT_FOUND),
    NOT_FOUND_TAG(404, "[ERROR] No such tag", HttpStatus.NOT_FOUND),;

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

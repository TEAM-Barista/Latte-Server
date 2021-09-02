package com.latte.server.common.exception;

import com.latte.server.common.exception.custom.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Minky on 2021-06-09
 */

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> customException(CustomException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(ExpiredTokenException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> expiredTokenException(ExpiredTokenException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> invalidTokenException(InvalidTokenException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(InvalidEmailOrPasswordException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> invalidEmailOrPasswordException(InvalidEmailOrPasswordException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(NotFoundEmailException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> notFoundEmailException(NotFoundEmailException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(NotFoundPasswordException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> notFoundPasswordException(NotFoundPasswordException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(NotFoundCategoryException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> notFoundCategoryException(NotFoundCategoryException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    @ExceptionHandler(NotFoundUserCategoryException.class)
    @ResponseBody
    ResponseEntity<ErrorBody> notFoundUserCategoryException(NotFoundUserCategoryException e) {
        ErrorBody errorBody = getErrorBody(e);
        return new ResponseEntity(errorBody, errorBody.getHttpStatus());
    }

    private ErrorBody getErrorBody(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        return new ErrorBody(
                errorCode.getCode(),
                errorCode.getMessage(),
                errorCode.getHttpStatus()
        );
    }
}

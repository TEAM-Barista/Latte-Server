package com.latte.server.common.exception;

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
    ResponseEntity<Map<String, Object>> handler(CustomException e) {
        HashMap<String, Object> responseBody = new HashMap();
        responseBody.put("error",
                new ErrorMessage(
                        e.getStatus().value(),
                        e.getMessage(),
                        e.getStatus()
                ));
        return new ResponseEntity(responseBody, e.getStatus());
    }
}

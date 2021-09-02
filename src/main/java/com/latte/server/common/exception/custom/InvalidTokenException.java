package com.latte.server.common.exception.custom;

import com.latte.server.common.exception.CustomException;
import com.latte.server.common.exception.ErrorCode;

/**
 * Created by Minky on 2021-08-11
 */
public class InvalidTokenException extends CustomException {
    public InvalidTokenException() { super(ErrorCode.INVALID_TOKEN); }
}

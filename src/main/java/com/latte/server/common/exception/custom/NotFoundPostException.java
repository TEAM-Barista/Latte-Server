package com.latte.server.common.exception.custom;

import com.latte.server.common.exception.CustomException;
import com.latte.server.common.exception.ErrorCode;

/**
 * Created by Donggun on 2021-11-24
 */
public class NotFoundPostException extends CustomException {
    public NotFoundPostException() { super(ErrorCode.NOT_FOUND_POST); }
}
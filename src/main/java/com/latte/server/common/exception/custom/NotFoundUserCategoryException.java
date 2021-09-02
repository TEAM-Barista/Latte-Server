package com.latte.server.common.exception.custom;

import com.latte.server.common.exception.CustomException;
import com.latte.server.common.exception.ErrorCode;

/**
 * Created by Minky on 2021-09-02
 */
public class NotFoundUserCategoryException extends CustomException {
    public NotFoundUserCategoryException() { super(ErrorCode.NOT_FOUND_USER_CATEGORY); }
}

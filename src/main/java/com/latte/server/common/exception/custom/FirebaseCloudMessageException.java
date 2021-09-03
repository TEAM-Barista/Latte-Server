package com.latte.server.common.exception.custom;

import com.latte.server.common.exception.CustomException;
import com.latte.server.common.exception.ErrorCode;

/**
 * Created by Minky on 2021-09-03
 */
public class FirebaseCloudMessageException extends CustomException {
    public FirebaseCloudMessageException() { super(ErrorCode.FCM_NOT_ALLOWED); }
}

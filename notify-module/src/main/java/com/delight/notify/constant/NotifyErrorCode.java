package com.delight.notify.constant;

import com.delight.gaia.base.vo.ResultCode;
import org.springframework.http.HttpStatus;

public interface NotifyErrorCode {
    ResultCode NOTIFY_APP_NOTFOUND = new ResultCode(301, "notify.appNotfound", HttpStatus.NOT_FOUND);
    ResultCode EMAIL_PROVIDER_NOT_FOUND = new ResultCode(304, "notify.emailProviderNotFound", HttpStatus.NOT_FOUND);
    ResultCode TOKEN_INVALID = new ResultCode(305, "notify.tokenInvalid", HttpStatus.BAD_REQUEST);

}

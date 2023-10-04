package com.delight.auth.constant;

import com.delight.gaia.base.vo.ResultCode;
import org.springframework.http.HttpStatus;

public interface ErrorCode {
    ResultCode LOGIN_FAIL = new ResultCode(20, "auth.loginFail", HttpStatus.FORBIDDEN);
    ResultCode OAUTH_PROVIDER_NOT_FOUND = new ResultCode(21, "auth.oauthProviderNotFound", HttpStatus.BAD_REQUEST);
    ResultCode OAUTH_EMAIL_NOT_FOUND = new ResultCode(22, "auth.oauthEmailNotFound", HttpStatus.BAD_REQUEST);
    ResultCode OLD_PASSWORD_WRONG = new ResultCode(23, "auth.oldPasswordWrong", HttpStatus.FORBIDDEN);
    ResultCode PHONE_EMAIL_EXISTED = new ResultCode(26, "auth.phoneOrEmailExisted", HttpStatus.CONFLICT);
    ResultCode ACCOUNT_NOT_FOUND = new ResultCode(27, "auth.accountNotFound", HttpStatus.CONFLICT);
    ResultCode APP_INVALID = new ResultCode(28, "auth.appInvalid", HttpStatus.CONFLICT);
    ResultCode OTP_INVALID = new ResultCode(29, "auth.otpInvalid", HttpStatus.FORBIDDEN);
    ResultCode KID_NOT_FOUND = new ResultCode(30, "auth.kidNotFound", HttpStatus.NOT_FOUND);
    ResultCode ACCOUNT_INACTIVE = new ResultCode(31, "auth.accountInactive", HttpStatus.FORBIDDEN);

}


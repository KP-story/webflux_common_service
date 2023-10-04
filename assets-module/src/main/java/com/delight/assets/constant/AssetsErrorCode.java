package com.delight.assets.constant;

import com.delight.gaia.base.vo.ResultCode;
import org.springframework.http.HttpStatus;

public interface AssetsErrorCode {
    ResultCode CONFIG_NOT_FOUND = new ResultCode(200, "assets.configNotFound", HttpStatus.NOT_FOUND);
    ResultCode PROVIDER_NOT_FOUND = new ResultCode(201, "assets.providerNotFound", HttpStatus.NOT_FOUND);
    ResultCode FILE_SIZE_INVALID = new ResultCode(202, "assets.fileSizeInvalid", HttpStatus.BAD_REQUEST);
    ResultCode CONTENT_TYPE_INVALID = new ResultCode(203, "assets.contentTypeInvalid", HttpStatus.BAD_REQUEST);

}

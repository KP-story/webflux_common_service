package com.delight.auth.api.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class VerifyOtpRS {
    private String verifyCode;
    private LocalDateTime expiredTime;
}

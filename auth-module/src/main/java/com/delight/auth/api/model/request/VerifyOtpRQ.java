package com.delight.auth.api.model.request;

import com.delight.auth.constant.OtpType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRQ {
    @NotNull
    private String account;
    @NotNull
    private String otp;
    @NotNull
    private OtpType type;
}

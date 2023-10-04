package com.delight.auth.api.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SetPasswordRQ {
    @NotNull
    private String password;
    @NotNull
    private String verificationCode;
    private String email;
    private String phone;
}

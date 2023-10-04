package com.delight.auth.api.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRQ {
    private String oldPassword;
    @NotNull
    private String newPassword;
    private Boolean keepLogin = false;
}

package com.delight.auth.api.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
public class LoginRQ {
    @NotNull
    @NotEmpty
    private String account;
    @NotNull
    @NotEmpty
    private String password;
}

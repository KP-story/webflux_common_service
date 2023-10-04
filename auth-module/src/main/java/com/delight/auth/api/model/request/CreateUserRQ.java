package com.delight.auth.api.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRQ {
    @NotNull
    @Email
    private String email;
    private String phone;
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
    private Short gender;
    @NotNull
    private String password;
}

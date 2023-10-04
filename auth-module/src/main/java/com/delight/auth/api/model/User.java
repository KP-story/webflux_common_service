package com.delight.auth.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private LocalDate birthday;
    private Boolean hasAvatar;
    private Short gender;
    private Long id;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    private Boolean hasPassword;
}

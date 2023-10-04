package com.delight.auth.api.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserPublicInfo extends UserViewInfo {
    private LocalDate birthday;
    private Short gender;
}

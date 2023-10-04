package com.delight.auth.api.model;

import com.delight.gaia.auth.subject.SubjectInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserSimpleInfo extends SubjectInfo {
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private LocalDate birthday;
    private Boolean hasAvatar;
    private Short gender;
    private String lang;
}

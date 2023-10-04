package com.delight.auth.api.model.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@Setter
public class UpdateUserRQ {
    private Optional<String> firstname;
    private Optional<String> lastname;
    private Optional<LocalDate> birthday;
    private Optional<Short> gender;
    private Optional<String> email;
    private Optional<String> phone;
}

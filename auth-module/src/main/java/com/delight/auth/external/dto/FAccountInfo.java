package com.delight.auth.external.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class FAccountInfo extends FError {
    private String id;
    private String name;
    private Picture picture;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
}

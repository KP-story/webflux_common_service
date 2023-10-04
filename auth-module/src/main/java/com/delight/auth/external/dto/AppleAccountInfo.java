package com.delight.auth.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleAccountInfo {
    private String iss;
    private String sub;
    private String aud;
    private Long iat;
    private Long exp;
    private String email;
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    @JsonProperty("at_hash")
    private String atHash;
    @JsonProperty("auth_time")
    private Long authTime;
    @JsonProperty("nonce_supported")
    private Boolean nonceSupported;
}

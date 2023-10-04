package com.delight.auth.api.model.request;

import com.delight.auth.constant.OAuthType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OAuthRQ {
    @NotNull
    private OAuthType provider = OAuthType.GOOGLE;
    @NotNull
    private String accessToken;
    private String authCode;
    private String firstName;
    private String lastName;
}

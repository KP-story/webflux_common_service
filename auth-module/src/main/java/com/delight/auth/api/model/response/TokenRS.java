package com.delight.auth.api.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class TokenRS {
    private String refreshToken;
    private String idToken;
    private LocalDateTime expiredTime;
}

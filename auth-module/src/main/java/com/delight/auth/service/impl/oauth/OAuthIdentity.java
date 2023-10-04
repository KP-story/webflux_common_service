package com.delight.auth.service.impl.oauth;

import com.delight.auth.constant.OAuthType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OAuthIdentity {
    private OAuthType oAuthType;
    private String id;
    private String profilePhoto;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String phone;
}

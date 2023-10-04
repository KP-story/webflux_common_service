package com.delight.auth.api.model.response;

import com.delight.auth.api.model.UserSimpleInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LoginRS extends TokenRS {
    private UserSimpleInfo simpleInfo;
}

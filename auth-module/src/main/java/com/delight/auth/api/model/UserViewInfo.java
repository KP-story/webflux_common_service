package com.delight.auth.api.model;

import com.delight.gaia.base.vo.UserDisplayInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserViewInfo extends UserDisplayInfo {
    private Long id;
}

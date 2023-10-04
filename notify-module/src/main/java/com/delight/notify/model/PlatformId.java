package com.delight.notify.model;

import com.delight.gaia.base.constant.Platform;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
public class PlatformId {
    String app;
    Platform platform;
}

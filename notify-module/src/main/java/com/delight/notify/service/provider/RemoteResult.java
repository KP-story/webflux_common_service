package com.delight.notify.service.provider;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RemoteResult {
    private boolean success;
    private Exception exception;
    private String reason;
}

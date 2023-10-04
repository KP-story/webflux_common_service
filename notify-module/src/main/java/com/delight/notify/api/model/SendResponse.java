package com.delight.notify.api.model;

import com.delight.notify.service.provider.RemoteResult;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SendResponse extends RemoteResult {
    private String transId;
}

package com.delight.notify.service.provider.notification.model;

import com.delight.notify.api.model.NotifyEnvelope;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class TokenNotifyEnv extends NotifyEnvelope {
    private List<String> tokens;
}

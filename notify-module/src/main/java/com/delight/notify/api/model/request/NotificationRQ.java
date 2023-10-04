package com.delight.notify.api.model.request;

import com.delight.notify.api.model.NotifyEnvelope;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class NotificationRQ {
    private boolean save;
    private Long customId;
    private String app;
    private NotifyEnvelope envelope;
}

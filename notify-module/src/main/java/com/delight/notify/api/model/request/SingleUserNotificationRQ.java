package com.delight.notify.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class SingleUserNotificationRQ extends NotificationRQ {
    Long userId;
}

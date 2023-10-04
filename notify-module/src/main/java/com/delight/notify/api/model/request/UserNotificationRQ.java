package com.delight.notify.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
public class UserNotificationRQ extends NotificationRQ {
    private List<Long> userIds;
}

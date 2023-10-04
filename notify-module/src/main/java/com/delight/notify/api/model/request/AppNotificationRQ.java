package com.delight.notify.api.model.request;

import com.delight.notify.constant.AppNotifyType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class AppNotificationRQ extends NotificationRQ {
    private AppNotifyType sendType = AppNotifyType.ALL;
}

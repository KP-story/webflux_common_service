package com.delight.notify.api.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserNotificationIgnoreDevicesRQ extends UserNotificationRQ {
    private List<String> ignoreDeviceIds;
}

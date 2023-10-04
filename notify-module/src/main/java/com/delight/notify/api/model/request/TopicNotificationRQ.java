package com.delight.notify.api.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class TopicNotificationRQ extends NotificationRQ {
    private List<String> topics;
}

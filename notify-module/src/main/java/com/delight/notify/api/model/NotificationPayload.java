package com.delight.notify.api.model;

import com.delight.notify.constant.NotifyScope;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class NotificationPayload {
    private Long id;
    private String type;
    private String appId;
    private String appCode;
    private String messageId;
    private Map<String, String> data;
    private String category;
    private LocalDateTime createdTime;
    private NotifyScope scope;
    private String title;
    private String body;
    private String icon;
    private String titleLocKey;
    private List<String> titleLocArgs;
    private String bodyLocKey;
    private List<String> bodyLocArgs;
}

package com.delight.notify.api.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class AppNotification {
    private Long notifyId;
    private String type;
    @JsonRawValue
    private String data;
    private String category;
    private LocalDateTime createdTime;
    private String title;
    private String body;
    private String icon;
    private String titleLocKey;
    private List<String> titleLocArgs;
    private String bodyLocKey;
    private List<String> bodyLocArgs;
}

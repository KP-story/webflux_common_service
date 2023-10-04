package com.delight.notify.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppInfo {
    private Long id;
    private String code;
    private String topic;
    private String loginTopic;
    private String config;
    private String key;
}

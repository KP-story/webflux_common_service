package com.delight.notify.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EmailConfig {
    private String code;
    private String providerType;
    private String defaultFrom;
    private String defaultFromName;
    private Map<String, String> parameters;
}

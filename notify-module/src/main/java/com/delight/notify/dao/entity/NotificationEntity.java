package com.delight.notify.dao.entity;

import com.delight.gaia.jpa.entity.BaseEntity;
import com.delight.notify.constant.NotifyScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationEntity extends BaseEntity {
    private String type;
    private String appId;
    private Long notificationId;
    private String data;
    private String category;
    private NotifyScope scope;
    private String title;
    private String body;
    private String icon;
    private String titleLocKey;
    private String[] titleLocArgs;
    private String bodyLocKey;
    private String[] bodyLocArgs;
}

package com.delight.notify.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(value = "user_notification")
public class UserNotificationEntity extends NotificationEntity {
    private Long userId;
    private boolean open;
    private String appCode;
    @CreatedDate
    private LocalDateTime createdTime;
}

package com.delight.notify.dao.entity;

import com.delight.gaia.jpa.entity.BaseEntity;
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
@Table(value = "user_notification_counter")
public class UserNotificationCounterEntity extends BaseEntity {
    private Long userId;
    private String appCode;
    private Integer unreadCount;
    @CreatedDate
    private LocalDateTime createdTime;
}

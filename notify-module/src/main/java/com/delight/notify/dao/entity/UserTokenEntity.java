package com.delight.notify.dao.entity;

import com.delight.gaia.base.constant.Platform;
import com.delight.gaia.jpa.entity.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Table(value = "user_token")
public class UserTokenEntity extends BaseEntity {
    private String token;
    private Long userId;
    private Long appId;
    private String appCode;
    private Platform platformCode;
    @CreatedDate
    private LocalDateTime createdTime;
    private String deviceId;
}

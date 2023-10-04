package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.TraceableEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Table(value = "refresh_token")
public class RefreshTokenEntity extends TraceableEntity {
    private String token;
    private LocalDateTime expiredTime;
    private String deviceId;
    private String deviceName;
    private String deviceOs;
    private Long userId;
    private String app;
    private String appVersion;
    private Double latitude;
    private Double longitude;
    private String platform;


}

package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(value = "otp")
public class OtpEntity extends BaseEntity {
    @CreatedDate
    private LocalDateTime createdTime;
    private LocalDateTime expiredTime;
    private String app;
    private String account;
    private String otp;
    private String type;
}

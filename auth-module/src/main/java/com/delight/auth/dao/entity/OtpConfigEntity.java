package com.delight.auth.dao.entity;

import com.delight.gaia.jpa.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(value = "otp_config")
public class OtpConfigEntity extends BaseEntity {
    private String app;
    private Integer otpLength;
    private Integer ttl;
    private String emailTemplate;
    private String smsTemplate;
    private String emailConfig;
    private String smsConfig;
    private String emailSubject;
    private Integer resendTime;
    private String otpType;
}

package com.delight.auth.service.impl;

import com.delight.auth.dao.entity.OtpConfigEntity;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.notify.api.model.EmailEnvelope;
import com.delight.notify.api.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class EmailSenderService {
    private TemplateResolver templateResolver;
    private EmailService emailService;

    public void sendEmailTemplate(OtpConfigEntity otpConfigEntity, String otp, UserEntity userEntity) {
        String emailBody = templateResolver.getEmailContentByTemplate(otpConfigEntity.getEmailTemplate(), Map.of("otp", otp, "name", userEntity.getFirstname() + " " + userEntity.getLastname()));
        EmailEnvelope emailEnvelope = new EmailEnvelope();
        emailEnvelope.setTo(new String[]{userEntity.getEmail()});
        emailEnvelope.setBody(emailBody);
        emailEnvelope.setSubject(otpConfigEntity.getEmailSubject());
        emailService.sendEmail(otpConfigEntity.getEmailConfig(), emailEnvelope).subscribe();
    }
}

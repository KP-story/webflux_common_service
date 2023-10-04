package com.delight.notify.service.provider.email.impl;

import com.delight.notify.api.model.EmailEnvelope;
import com.delight.notify.model.EmailConfig;
import com.delight.notify.service.provider.RemoteResult;
import com.delight.notify.service.provider.email.EmailProvider;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component("SMTP_EMAIL")
public class SmtpProvider implements EmailProvider {
    Cache<String, JavaMailSender> javaMailSenders = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.HOURS).expireAfterAccess(5, TimeUnit.HOURS).scheduler(Scheduler.systemScheduler())
            .maximumSize(100)
            .build();

    protected JavaMailSender javaMailSender(EmailConfig emailConfig) {
        return javaMailSenders.get(emailConfig.getCode(), s -> {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(emailConfig.getParameters().get("host"));
            mailSender.setPort(587);
            mailSender.setUsername(emailConfig.getParameters().get("user"));
            mailSender.setPassword(emailConfig.getParameters().get("password"));
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "false");
            return mailSender;
        });

    }

    @Override
    public Mono<RemoteResult> send(EmailConfig emailConfig, EmailEnvelope emailEnvelope) {


        return Mono.fromCallable(() -> {
            try {
                JavaMailSender javaMailSender = javaMailSender(emailConfig);
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                if (emailEnvelope.getBcc() != null)
                    helper.setBcc(emailEnvelope.getBcc());
                if (emailEnvelope.getTo() != null)
                    helper.setTo(emailEnvelope.getTo());
                if (emailEnvelope.getCc() != null)
                    helper.setCc(emailEnvelope.getCc());
                helper.setSubject(emailEnvelope.getSubject());
                if (emailEnvelope.getFrom() != null) {
                    if (emailEnvelope.getFromName() != null)
                        helper.setFrom(emailEnvelope.getFrom(), emailEnvelope.getFromName());
                    else
                        helper.setFrom(emailEnvelope.getFrom());
                } else if (emailConfig.getDefaultFrom() != null) {
                    if (emailConfig.getDefaultFromName() != null)
                        helper.setFrom(emailConfig.getDefaultFrom(), emailConfig.getDefaultFromName());
                    else
                        helper.setFrom(emailConfig.getDefaultFrom());
                }

                helper.setText(emailEnvelope.getBody(), emailEnvelope.isHtml());
                javaMailSender.send(message);
                return new RemoteResult().setSuccess(true);
            } catch (Exception e) {
                log.error("send mail with  config {} envelop {} error", emailConfig, emailEnvelope, e);
                return new RemoteResult().setSuccess(false).setException(e);

            }
        });
    }
}

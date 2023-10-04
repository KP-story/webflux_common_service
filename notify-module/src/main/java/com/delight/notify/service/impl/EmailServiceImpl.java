package com.delight.notify.service.impl;

import com.delight.gaia.base.exception.CommandFailureException;
import com.delight.gaia.base.utility.TokenUtils;
import com.delight.notify.api.model.EmailEnvelope;
import com.delight.notify.api.service.EmailService;
import com.delight.notify.dao.repo.EmailProviderRepo;
import com.delight.notify.mapper.EmailConfigMapper;
import com.delight.notify.model.EmailConfig;
import com.delight.notify.service.provider.email.EmailProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.delight.notify.constant.NotifyErrorCode.EMAIL_PROVIDER_NOT_FOUND;

@Component
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {
    private Map<String, EmailProvider> emailProviders;
    private EmailConfigMapper emailConfigMapper;
    private EmailProviderRepo emailProviderRepo;

    private final String genTransId() {
        return TokenUtils.generateUUIdToken();
    }

    @Override
    public Mono<String> sendEmail(String provider, EmailEnvelope emailEnvelope) {
        return getEmailConfig(provider).switchIfEmpty(Mono.defer(() ->
                Mono.error(new CommandFailureException(EMAIL_PROVIDER_NOT_FOUND))
        )).flatMap(emailConfig -> emailProviders.get(emailConfig.getProviderType()).send(emailConfig, emailEnvelope).map(remoteResult -> genTransId()));
    }

    Mono<EmailConfig> getEmailConfig(String provider) {
        return emailProviderRepo.findByCode(provider).map(emailConfigMapper::entityToInfo);
    }
}

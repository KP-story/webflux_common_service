package com.delight.notify.service.provider.email;

import com.delight.notify.api.model.EmailEnvelope;
import com.delight.notify.model.EmailConfig;
import com.delight.notify.service.provider.RemoteResult;
import reactor.core.publisher.Mono;

public interface EmailProvider {
    Mono<RemoteResult> send(EmailConfig emailConfig, EmailEnvelope emailEnvelope);
}

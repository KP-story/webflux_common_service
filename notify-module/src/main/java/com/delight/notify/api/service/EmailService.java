package com.delight.notify.api.service;

import com.delight.notify.api.model.EmailEnvelope;
import reactor.core.publisher.Mono;

public interface EmailService {
    Mono<String> sendEmail(String provider, EmailEnvelope emailEnvelope);
}

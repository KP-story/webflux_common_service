package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.JPABaseRepo;
import com.delight.notify.dao.entity.EmailProviderEntity;
import reactor.core.publisher.Mono;

public interface EmailProviderJpaRepo extends JPABaseRepo<EmailProviderEntity, Long> {
    Mono<EmailProviderEntity> findByCode(String code);
}

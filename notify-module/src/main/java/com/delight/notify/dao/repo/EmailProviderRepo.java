package com.delight.notify.dao.repo;

import com.delight.gaia.jpa.repo.BaseRepo;
import com.delight.notify.dao.entity.EmailProviderEntity;
import reactor.core.publisher.Mono;

public interface EmailProviderRepo extends BaseRepo<EmailProviderEntity, Long> {
    Mono<EmailProviderEntity> findByCode(String code);
}

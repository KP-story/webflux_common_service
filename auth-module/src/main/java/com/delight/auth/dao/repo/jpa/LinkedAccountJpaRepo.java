package com.delight.auth.dao.repo.jpa;

import com.delight.auth.constant.ProviderType;
import com.delight.auth.dao.entity.LinkedAccountEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import reactor.core.publisher.Mono;

public interface LinkedAccountJpaRepo extends JPABaseRepo<LinkedAccountEntity, Long> {
    Mono<LinkedAccountEntity> findByUserIdAndProviderTypeAndProviderId(Long userId, ProviderType providerType, String providerID);

    Mono<LinkedAccountEntity> findByUserIdAndProviderType(Long userId, ProviderType providerType);

    Mono<LinkedAccountEntity> findByProviderTypeAndProviderId(ProviderType providerType, String providerID);

    Mono<Boolean> existsByUserIdAndProviderType(Long userId, ProviderType providerType);
}

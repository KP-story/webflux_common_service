package com.delight.auth.dao.repo;

import com.delight.auth.constant.ProviderType;
import com.delight.auth.dao.entity.LinkedAccountEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Mono;

public interface LinkedAccountRepo extends BaseRepo<LinkedAccountEntity, Long> {
    Mono<LinkedAccountEntity> findByUserIdAndTypeAndProviderId(Long userId, ProviderType type, String providerId);

    Mono<LinkedAccountEntity> findByTypeAndProviderId(ProviderType type, String providerId);

    Mono<LinkedAccountEntity> findByUserIdAndProviderType(Long userId, ProviderType providerType);

    Mono<Boolean> existByUserIdAndProviderType(Long userId, ProviderType providerType);

}

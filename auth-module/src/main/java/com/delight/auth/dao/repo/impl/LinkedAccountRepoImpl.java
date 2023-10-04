package com.delight.auth.dao.repo.impl;

import com.delight.auth.constant.ProviderType;
import com.delight.auth.dao.entity.LinkedAccountEntity;
import com.delight.auth.dao.repo.LinkedAccountRepo;
import com.delight.auth.dao.repo.jpa.LinkedAccountJpaRepo;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class LinkedAccountRepoImpl extends AbsBaseRepo<LinkedAccountEntity, Long, LinkedAccountJpaRepo> implements LinkedAccountRepo {
    @Override
    public Mono<LinkedAccountEntity> findByUserIdAndTypeAndProviderId(Long userId, ProviderType type, String providerId) {
        return jpaRepo.findByUserIdAndProviderTypeAndProviderId(userId, type, providerId);
    }

    @Override
    public Mono<LinkedAccountEntity> findByTypeAndProviderId(ProviderType type, String providerId) {
        return jpaRepo.findByProviderTypeAndProviderId(type, providerId);
    }

    @Override
    public Mono<LinkedAccountEntity> findByUserIdAndProviderType(Long userId, ProviderType providerType) {
        return jpaRepo.findByUserIdAndProviderType(userId, providerType);
    }

    @Override
    public Mono<Boolean> existByUserIdAndProviderType(Long userId, ProviderType providerType) {
        return jpaRepo.existsByUserIdAndProviderType(userId, providerType);
    }
}

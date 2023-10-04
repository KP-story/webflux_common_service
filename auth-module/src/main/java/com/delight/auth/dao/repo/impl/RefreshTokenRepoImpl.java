package com.delight.auth.dao.repo.impl;

import com.delight.auth.dao.entity.RefreshTokenEntity;
import com.delight.auth.dao.repo.RefreshTokenRepo;
import com.delight.auth.dao.repo.jpa.RefreshTokenJpaRepo;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public class RefreshTokenRepoImpl extends AbsBaseRepo<RefreshTokenEntity, Long, RefreshTokenJpaRepo> implements RefreshTokenRepo {


    @Override
    public Flux<String> deleteByDeviceIdAndUserIdIgnoreIdOrExceedSession(String deviceId, Long userId, Long ignoreId, Short maxSession, String app) {
        return jpaRepo.deleteExceedSession(deviceId, userId, maxSession, app, ignoreId);
    }

    @Override
    public Mono<Void> deleteByDeviceIdAndUserId(String deviceId, Long userId, String app) {
        return jpaRepo.deleteAllByUserIdAndDeviceIdAndApp(userId, deviceId, app);
    }

    @Override
    public Mono<Void> deleteByUserId(Long userId, String app) {
        return jpaRepo.deleteByUserIdAndApp(userId, app);
    }

    @Override
    public Mono<RefreshTokenEntity> findActiveByToken(String token, String app) {
        return jpaRepo.findByTokenAndExpiredTimeAfterAndApp(token, LocalDateTime.now(), app);
    }

    @Override
    public Mono<Void> deleteBefore(LocalDateTime localDateTime) {
        return jpaRepo.deleteByExpiredTimeBefore(localDateTime);
    }
}

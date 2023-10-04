package com.delight.auth.dao.repo;

import com.delight.auth.dao.entity.RefreshTokenEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RefreshTokenRepo extends BaseRepo<RefreshTokenEntity, Long> {
    Flux<String> deleteByDeviceIdAndUserIdIgnoreIdOrExceedSession(String deviceId, Long userId, Long ignoreId, Short maxSession, String app);

    Mono<Void> deleteByDeviceIdAndUserId(String deviceId, Long userId, String app);

    Mono<Void> deleteByUserId(Long userId, String app);

    Mono<RefreshTokenEntity> findActiveByToken(String token, String app);

    Mono<Void> deleteBefore(LocalDateTime localDateTime);
}

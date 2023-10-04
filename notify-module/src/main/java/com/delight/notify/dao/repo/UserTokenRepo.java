package com.delight.notify.dao.repo;

import com.delight.gaia.jpa.repo.BaseRepo;
import com.delight.notify.dao.entity.UserTokenEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface UserTokenRepo extends BaseRepo<UserTokenEntity, Long> {

    Mono<UserTokenEntity> findByToken(String token, String appCode);

    Flux<UserTokenEntity> findByUserAndDeviceIds(Long userId, List<String> deviceIds, String appCode);

    Flux<UserTokenEntity> findByUser(Long userId, String appCode);

    Flux<UserTokenEntity> findByUser(List<Long> userIds, String appCode);

    Flux<UserTokenEntity> findByUserIgnoreDevices(List<Long> userIds, String appCode, List<String> deviceIds);

    Flux<String> deleteExceedToken(Long userId, String token, String appCode, Short maxSession, String deviceId);

    Mono<Void> deleteToken(String token, String appCode);

    Mono<Void> deleteByTokenIds(List<Long> tokenIds, String appCode);

    Mono<Void> deleteByTokens(List<String> tokenIds, String appCode);

    Mono<Void> deleteBefore(LocalDateTime localDateTime);

}

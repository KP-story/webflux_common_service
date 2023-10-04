package com.delight.notify.dao.repo.impl;

import com.delight.gaia.jpa.repo.AbsBaseRepo;
import com.delight.notify.dao.entity.UserTokenEntity;
import com.delight.notify.dao.repo.UserTokenRepo;
import com.delight.notify.dao.repo.jpa.UserTokenJpaRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserTokenRepoImpl extends AbsBaseRepo<UserTokenEntity, Long, UserTokenJpaRepo> implements UserTokenRepo {

    @Override
    public Mono<UserTokenEntity> findByToken(String token, String appCode) {
        return jpaRepo.findByTokenAndAppCode(token, appCode);
    }

    @Override
    public Flux<UserTokenEntity> findByUserAndDeviceIds(Long userId, List<String> deviceIds, String appCode) {
        return jpaRepo.findAllByDeviceIdInAndUserIdAndAppCode(deviceIds, userId, appCode);
    }

    @Override
    public Flux<UserTokenEntity> findByUser(Long userId, String appCode) {
        return jpaRepo.findAllByUserIdAndAppCode(userId, appCode);
    }

    @Override
    public Flux<UserTokenEntity> findByUser(List<Long> userIds, String appCode) {
        return jpaRepo.findAllByUserIdInAndAppCode(userIds, appCode);
    }

    @Override
    public Flux<UserTokenEntity> findByUserIgnoreDevices(List<Long> userIds, String appCode, List<String> deviceIds) {
        if(deviceIds!=null&&!deviceIds.isEmpty())
        return jpaRepo.findAllByUserIdInAndAppCodeAndDeviceIdNotIn(userIds, appCode, deviceIds);
        else
        return jpaRepo.findAllByUserIdInAndAppCode(userIds, appCode);
    }

    @Override
    public Flux<String> deleteExceedToken(Long userId, String token, String appCode, Short maxSession, String deviceId) {
        return jpaRepo.deleteByTokenAndAppCode(token, appCode, maxSession, deviceId, userId);
    }

    @Override
    public Mono<Void> deleteToken(String token, String appCode) {
        return jpaRepo.deleteByTokenAndAppCode(token, appCode);
    }

    @Override
    public Mono<Void> deleteByTokenIds(List<Long> tokenIds, String appCode) {
        return jpaRepo.deleteByIdInAndAppCode(tokenIds, appCode);
    }

    @Override
    public Mono<Void> deleteByTokens(List<String> tokenIds, String appCode) {
        return null;
    }

    @Override
    public Mono<Void> deleteBefore(LocalDateTime localDateTime) {
        return jpaRepo.deleteByCreatedTimeBefore(localDateTime);
    }
}

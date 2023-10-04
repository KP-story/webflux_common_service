package com.delight.auth.dao.repo.jpa;

import com.delight.auth.dao.entity.OtpEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface OtpJpaRepo extends JPABaseRepo<OtpEntity, Long> {
    Mono<OtpEntity> findFirstByAccountAndTypeAndAppAndExpiredTimeAfterOrderByCreatedTimeDesc(String account, String type, String app, LocalDateTime localDateTime);

    Mono<OtpEntity> findFirstByAccountAndTypeAndAppAndExpiredTimeAfterAndOtpOrderByCreatedTimeDesc(String account, String type, String app, LocalDateTime localDateTime, String otp);

    Mono<Void> deleteByExpiredTimeBefore(LocalDateTime now);

    Mono<Void> deleteByAccountAndTypeAndApp(String account, String type, String appa);

}

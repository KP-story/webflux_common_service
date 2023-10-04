package com.delight.auth.dao.repo;

import com.delight.auth.dao.entity.OtpEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface OtpRepo extends BaseRepo<OtpEntity, Long> {
    Mono<OtpEntity> findActiveOtp(String account, String type, String app, LocalDateTime localDateTime);

    Mono<OtpEntity> findActiveOtp(String account, String type, String app, LocalDateTime localDateTime, String otp);

    Mono<Void> deleteBefore(LocalDateTime localDateTime);

    Mono<Void> deleteByAccountAndTypeAndApp(String account, String type, String app);

}

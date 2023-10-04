package com.delight.auth.dao.repo.impl;

import com.delight.auth.dao.entity.OtpEntity;
import com.delight.auth.dao.repo.OtpRepo;
import com.delight.auth.dao.repo.jpa.OtpJpaRepo;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
public class OtpRepoImpl extends AbsBaseRepo<OtpEntity, Long, OtpJpaRepo> implements OtpRepo {
    @Override
    public Mono<OtpEntity> findActiveOtp(String account, String type, String app, LocalDateTime localDateTime) {
        return jpaRepo.findFirstByAccountAndTypeAndAppAndExpiredTimeAfterOrderByCreatedTimeDesc(account, type, app, localDateTime);
    }

    @Override
    public Mono<OtpEntity> findActiveOtp(String account, String type, String app, LocalDateTime localDateTime, String otp) {
        return jpaRepo.findFirstByAccountAndTypeAndAppAndExpiredTimeAfterAndOtpOrderByCreatedTimeDesc(account, type, app, localDateTime, otp);
    }

    @Override
    public Mono<Void> deleteBefore(LocalDateTime localDateTime) {
        return jpaRepo.deleteByExpiredTimeBefore(localDateTime);
    }

    @Override
    public Mono<Void> deleteByAccountAndTypeAndApp(String account, String type, String app) {
        return jpaRepo.deleteByAccountAndTypeAndApp(account, type, app);
    }
}

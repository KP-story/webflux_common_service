package com.delight.auth.dao.repo.jpa;

import com.delight.auth.dao.entity.OtpConfigEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import reactor.core.publisher.Mono;

public interface OtpConfigJpaRepo extends JPABaseRepo<OtpConfigEntity, Long> {
    Mono<OtpConfigEntity> findAllByAppAndOtpType(String app, String otpType);
}

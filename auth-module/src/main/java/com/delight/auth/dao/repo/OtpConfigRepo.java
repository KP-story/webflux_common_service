package com.delight.auth.dao.repo;

import com.delight.auth.constant.OtpType;
import com.delight.auth.dao.entity.OtpConfigEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Mono;

public interface OtpConfigRepo extends BaseRepo<OtpConfigEntity, Long> {
    Mono<OtpConfigEntity> findByApp(String app, OtpType otpType);
}

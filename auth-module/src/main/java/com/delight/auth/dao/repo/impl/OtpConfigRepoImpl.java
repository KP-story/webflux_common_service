package com.delight.auth.dao.repo.impl;

import com.delight.auth.constant.OtpType;
import com.delight.auth.dao.entity.OtpConfigEntity;
import com.delight.auth.dao.repo.OtpConfigRepo;
import com.delight.auth.dao.repo.jpa.OtpConfigJpaRepo;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class OtpConfigRepoImpl extends AbsBaseRepo<OtpConfigEntity, Long, OtpConfigJpaRepo> implements OtpConfigRepo {
    @Override
    public Mono<OtpConfigEntity> findByApp(String app, OtpType otpType) {
        return jpaRepo.findAllByAppAndOtpType(app, otpType.name());
    }
}

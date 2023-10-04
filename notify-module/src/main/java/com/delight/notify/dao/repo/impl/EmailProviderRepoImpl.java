package com.delight.notify.dao.repo.impl;

import com.delight.gaia.jpa.repo.AbsBaseRepo;
import com.delight.notify.dao.entity.EmailProviderEntity;
import com.delight.notify.dao.repo.EmailProviderRepo;
import com.delight.notify.dao.repo.jpa.EmailProviderJpaRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class EmailProviderRepoImpl extends AbsBaseRepo<EmailProviderEntity, Long, EmailProviderJpaRepo> implements EmailProviderRepo {
    @Override
    public Mono<EmailProviderEntity> findByCode(String code) {
        return jpaRepo.findByCode(code);
    }
}

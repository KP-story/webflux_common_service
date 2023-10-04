package com.delight.auth.dao.repo.impl;

import com.delight.auth.dao.entity.AllowOriginEntity;
import com.delight.auth.dao.repo.AllowOriginRepo;
import com.delight.auth.dao.repo.jpa.AllowOriginJpaRepo;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class AllowOriginRepoImpl extends AbsBaseRepo<AllowOriginEntity, Long, AllowOriginJpaRepo> implements AllowOriginRepo {

    @Override
    public Mono<Boolean> existsOrigin(String origin) {
        return jpaRepo.existsByOrigin(origin);
    }
}

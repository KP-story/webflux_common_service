package com.delight.auth.dao.repo.jpa;

import com.delight.auth.dao.entity.AllowOriginEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import reactor.core.publisher.Mono;

public interface AllowOriginJpaRepo extends JPABaseRepo<AllowOriginEntity, Long> {
    Mono<Boolean> existsByOrigin(String origin);
}

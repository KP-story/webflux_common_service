package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.JPABaseRepo;
import com.delight.notify.dao.entity.AppEntity;
import reactor.core.publisher.Mono;

public interface AppJpaRepo extends JPABaseRepo<AppEntity, Long> {

    Mono<AppEntity> findByCode(String appCode);


}

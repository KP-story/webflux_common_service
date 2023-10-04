package com.delight.notify.dao.repo;

import com.delight.gaia.jpa.repo.BaseRepo;
import com.delight.notify.dao.entity.AppEntity;
import reactor.core.publisher.Mono;

public interface AppRepo extends BaseRepo<AppEntity, Long> {
    Mono<AppEntity> findByCode(String appCode);
}

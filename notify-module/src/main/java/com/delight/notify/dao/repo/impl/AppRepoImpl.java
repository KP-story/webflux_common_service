package com.delight.notify.dao.repo.impl;

import com.delight.gaia.jpa.repo.AbsBaseRepo;
import com.delight.notify.dao.entity.AppEntity;
import com.delight.notify.dao.repo.AppRepo;
import com.delight.notify.dao.repo.jpa.AppJpaRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class AppRepoImpl extends AbsBaseRepo<AppEntity, Long, AppJpaRepo> implements AppRepo {
    @Override
    public Mono<AppEntity> findByCode(String appCode) {
        return jpaRepo.findByCode(appCode);
    }
}

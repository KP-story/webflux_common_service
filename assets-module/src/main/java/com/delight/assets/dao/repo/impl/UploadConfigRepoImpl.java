package com.delight.assets.dao.repo.impl;

import com.delight.assets.dao.entity.UploadConfigEntity;
import com.delight.assets.dao.repo.UploadConfigRepo;
import com.delight.assets.dao.repo.jpa.UploadJpaRepo;
import com.delight.gaia.base.constant.Status;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class UploadConfigRepoImpl extends AbsBaseRepo<UploadConfigEntity, Long, UploadJpaRepo> implements UploadConfigRepo {
    @Override
    public Mono<UploadConfigEntity> findActiveConfig(String code) {
        return jpaRepo.findByStatusAndCode(Status.ACTIVE, code);
    }

    @Override
    public Flux<UploadConfigEntity> findActiveConfigByBucket(String bucket) {
        return jpaRepo.findByStatusAndBucket(Status.ACTIVE,bucket);
    }

    @Override
    public Flux<UploadConfigEntity> findActiveConfigs(List<String> codes) {
        return jpaRepo.findByStatusAndCodeIn(Status.ACTIVE, codes);
    }
}

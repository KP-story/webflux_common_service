package com.delight.assets.dao.repo.jpa;

import com.delight.assets.dao.entity.UploadConfigEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UploadJpaRepo extends JPABaseRepo<UploadConfigEntity, Long> {

    Mono<UploadConfigEntity> findByStatusAndCode(Byte status, String code);

    Flux<UploadConfigEntity> findByStatusAndCodeIn(Byte status, List<String> code);
    Flux<UploadConfigEntity> findByStatusAndBucket(Byte status, String bucket);

}

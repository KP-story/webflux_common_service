package com.delight.assets.dao.repo;

import com.delight.assets.dao.entity.UploadConfigEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UploadConfigRepo extends BaseRepo<UploadConfigEntity, Long> {
    Mono<UploadConfigEntity> findActiveConfig(String code);
    Flux<UploadConfigEntity> findActiveConfigByBucket(String bucket);

    Flux<UploadConfigEntity> findActiveConfigs(List<String> codes);
}

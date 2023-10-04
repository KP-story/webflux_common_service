package com.delight.auth.dao.repo;

import com.delight.auth.dao.entity.PermissionEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Flux;

public interface PermissionRepo extends BaseRepo<PermissionEntity, Long> {
    Flux<String> findAllUserPermission(Long userId);
}

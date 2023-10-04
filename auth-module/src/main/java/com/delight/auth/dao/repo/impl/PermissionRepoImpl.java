package com.delight.auth.dao.repo.impl;

import com.delight.auth.dao.entity.PermissionEntity;
import com.delight.auth.dao.repo.PermissionRepo;
import com.delight.auth.dao.repo.jpa.PermissionJpaRepo;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class PermissionRepoImpl extends AbsBaseRepo<PermissionEntity, Long, PermissionJpaRepo> implements PermissionRepo {

    @Override
    public Flux<String> findAllUserPermission(Long userId) {
        return jpaRepo.findAllUserPermission(userId).filter(permissionEntity -> permissionEntity != null || permissionEntity.getCode() == null).map(permissionEntity -> permissionEntity.getCode());
    }
}

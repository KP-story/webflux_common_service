package com.delight.auth.dao.repo.jpa;

import com.delight.auth.dao.entity.PermissionEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;

public interface PermissionJpaRepo extends JPABaseRepo<PermissionEntity, Long> {

    @Query("select p.*  from se_permission p join  role_permission up on p.id =up.permission_id join " +
            "user_role ur on up.role_id =ur.role_id where ur.user_id = $1 and p.status =1 ")
    Flux<PermissionEntity> findAllUserPermission(Long userId);

}

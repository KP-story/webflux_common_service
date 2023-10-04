package com.delight.auth.cache;

import com.delight.auth.cache.dao.UserKey;
import com.delight.auth.dao.repo.PermissionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.delight.gaia.concurrent.cache.CacheConfig;
import vn.delight.gaia.concurrent.cache.CacheManager;
import vn.delight.gaia.concurrent.cache.CacheService;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@Component
public class PermissionCache extends CacheService<UserKey, Set<String>> {
    @Autowired
    private PermissionRepo permissionRepo;

    public PermissionCache(CacheManager cacheManager) {
        super(cacheManager, "user.permissions");
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setIdleTime(600000L);
        cacheConfig.setTtl(6 * 60 * 60L);
        cacheConfig.setMaxSize(100000L);
        Function loader = (Function<UserKey, Mono<Set<String>>>) userKey -> permissionRepo.findAllUserPermission(userKey.getUserId()).collectList().map(permissions -> new HashSet<>(permissions));
        cacheConfig.setLoader(loader);
        setCacheConfig(cacheConfig);
        init();
    }

}

package com.delight.auth.cache;

import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.delight.gaia.concurrent.cache.CacheConfig;
import vn.delight.gaia.concurrent.cache.CacheManager;
import vn.delight.gaia.concurrent.cache.CacheService;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Component
public class UserDisplayCache extends CacheService<Long, UserViewInfo> {
    @Autowired
    private UserRepo userRepo;

    public UserDisplayCache(CacheManager cacheManager) {
        super(cacheManager, "userDisplay");
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setIdleTime(600000L);
        cacheConfig.setTtl(6 * 60 * 60L);
        cacheConfig.setMaxSize(100000L);
        Function loader = (Function<Long, Mono<UserViewInfo>>) userKey -> userRepo.findUserViewInfo(userKey);
        Function multipleLoader = (Function<Set<Long>, Flux<Map.Entry<Long, UserViewInfo>>>) userKey ->
                userRepo.listUserViewInfo(userKey).map(userViewInfo -> Map.entry(userViewInfo.getId(), userViewInfo));
        cacheConfig.setLoader(loader);
        cacheConfig.setMultipleLoader(multipleLoader);
        setCacheConfig(cacheConfig);
        init();
    }

}


package com.delight.auth.cache;

import com.delight.auth.dao.repo.AllowOriginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.delight.gaia.concurrent.cache.CacheConfig;
import vn.delight.gaia.concurrent.cache.CacheManager;
import vn.delight.gaia.concurrent.cache.CacheService;

import java.util.function.Function;

@Component
public class AllowOriginCache extends CacheService<String, Boolean> {
    @Autowired
    private AllowOriginRepo allowOriginRepo;

    public AllowOriginCache(CacheManager cacheManager) {
        super(cacheManager, "allow_origins");
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setIdleTime(600000L);
        cacheConfig.setTtl(6 * 60 * 60L);
        cacheConfig.setMaxSize(100000L);
        Function loader = (Function<String, Mono<Boolean>>) origin -> allowOriginRepo.existsOrigin(origin);
        cacheConfig.setLoader(loader);
        setCacheConfig(cacheConfig);
        init();
    }
}

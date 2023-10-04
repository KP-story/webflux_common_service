package com.delight.notify.cache;

import com.delight.notify.dao.repo.AppRepo;
import com.delight.notify.mapper.AppInfoMapper;
import com.delight.notify.model.AppInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.delight.gaia.concurrent.cache.CacheConfig;
import vn.delight.gaia.concurrent.cache.CacheManager;
import vn.delight.gaia.concurrent.cache.CacheService;

import java.util.function.Function;

@Component
public class AppCache
        extends CacheService<String, AppInfo> {
    @Autowired
    private AppRepo appRepo;
    @Autowired
    private AppInfoMapper appInfoMapper;

    public AppCache(CacheManager cacheManager) {
        super(cacheManager, "app");
        CacheConfig cacheConfig = new CacheConfig();
        cacheConfig.setIdleTime(600000L);
        cacheConfig.setTtl(6 * 60 * 60L);
        cacheConfig.setMaxSize(100000L);
        Function loader = (Function<String, Mono<AppInfo>>) appCode -> appRepo.findByCode(appCode).map(appInfoMapper::entityToInfo);
        cacheConfig.setLoader(loader);
        setCacheConfig(cacheConfig);
        init();
    }

}

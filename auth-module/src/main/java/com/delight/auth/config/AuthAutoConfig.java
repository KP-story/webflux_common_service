package com.delight.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.delight.gaia.concurrent.cache.CacheManager;
import vn.delight.gaia.concurrent.cache.caffeine.CaffeineCacheManager;

@Configuration
@EnableConfigurationProperties(AuthConfig.class)
public class AuthAutoConfig {
    @Bean
    public CacheManager localCache() {
        return new CaffeineCacheManager("auth");
    }
}

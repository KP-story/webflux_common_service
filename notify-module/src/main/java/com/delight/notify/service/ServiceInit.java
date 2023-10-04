package com.delight.notify.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ServiceInit {
    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(1);
    }
}

package com.delight.auth.dao.repo;

import reactor.core.publisher.Mono;

public interface AllowOriginRepo {
    Mono<Boolean> existsOrigin(String origin);
}

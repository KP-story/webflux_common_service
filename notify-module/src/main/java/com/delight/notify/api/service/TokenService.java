package com.delight.notify.api.service;

import reactor.core.publisher.Mono;

import java.util.List;

public interface TokenService {
    Mono<Void> registerToken(String token);

    Mono<Void> unregisterDeviceIds(List<String> deviceId, Long userId, String app);

}

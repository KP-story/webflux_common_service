package com.delight.notify.api.service;

import com.delight.notify.api.model.UserNotification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserNotifyService {
    Flux<UserNotification> loadForwardUserNotification(String type, String category, Long lastId, int pageSize);

    Flux<UserNotification> loadBackwardUserNotification(String type, String category, Long firstId, int pageSize);


    Mono<Void> markRead(Long notifyId);

    Mono<Void> markReadAll();
    Mono<Void> resetCounter();

    Mono<Integer> countNotify();

}

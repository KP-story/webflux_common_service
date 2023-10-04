package com.delight.notify.dao.repo;

import com.delight.gaia.jpa.repo.BaseRepo;
import com.delight.notify.api.model.UserNotification;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationCounterEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface UserNotificationCounterRepo extends BaseRepo<UserNotificationCounterEntity, Long> {
    Mono<Integer> countUnreadNotify(Long userId, String app);

    Mono<Void> resetCounter(Long userId, String app);
    Mono<Void> increaseCounter(Long userId, String app);
    Mono<Void> increaseCounter(List<Long> userIds, String app);
}

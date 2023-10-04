package com.delight.notify.dao.repo;

import com.delight.gaia.jpa.repo.BaseRepo;
import com.delight.notify.api.model.UserNotification;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface UserNotificationRepo extends BaseRepo<UserNotificationEntity, Long> {
    Mono<Long> countUnreadNotify(Long userId, String app);

    Mono<Void> markUnread(Long userId, Long id, String app);

    Mono<Void> markAllUnread(Long userId, String app);

    Flux<UserNotification> loadBackwardUserNotify(Long userId, String app, String type, String category, Long lastNotifyId, int pageSize);

    Flux<UserNotification> loadForwardUserNotify(Long userId, String app, String type, String category, Long firstId, int pageSize);


    Mono<Void> deleteBefore(LocalDateTime localDateTime);

    Mono<NotificationEntity> saveBatch(List<Long> userIds, Long notifyId, String app, NotificationEntity notificationEntity);

}

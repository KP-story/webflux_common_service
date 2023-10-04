package com.delight.notify.dao.repo.impl;

import com.delight.gaia.jpa.repo.AbsBaseRepo;
import com.delight.notify.api.model.UserNotification;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import com.delight.notify.dao.repo.UserNotificationRepo;
import com.delight.notify.dao.repo.jpa.UserNotificationBatchRepo;
import com.delight.notify.dao.repo.jpa.UserNotificationJpaRepo;
import com.delight.notify.dao.repo.jpa.custom.UserNotificationQueryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@AllArgsConstructor
public class UserNotificationRepoImpl extends AbsBaseRepo<UserNotificationEntity, Long, UserNotificationJpaRepo> implements UserNotificationRepo {
    private UserNotificationQueryRepo userNotificationQueryRepo;
    private UserNotificationBatchRepo notificationBatchRepo;

    @Override
    public Mono<Long> countUnreadNotify(Long userId, String app) {
        return jpaRepo.countUnreadNotify(app, userId);
    }

    @Override
    public Mono<Void> markUnread(Long userId, Long id, String app) {
        return jpaRepo.updateOpen(app, id, userId);
    }

    @Override
    public Mono<Void> markAllUnread(Long userId, String app) {
        return jpaRepo.updateAllOpen(app, userId);
    }

    @Override
    public Flux<UserNotification> loadBackwardUserNotify(Long userId, String app, String type, String category, Long lastNotifyId, int pageSize) {
        return userNotificationQueryRepo.loadBackwardUserNotify(userId, app, type, category, lastNotifyId, pageSize);
    }

    @Override
    public Flux<UserNotification> loadForwardUserNotify(Long userId, String app, String type, String category, Long firstId, int pageSize) {
        return userNotificationQueryRepo.loadForwardUserNotify(userId, app, type, category, firstId, pageSize);
    }


    @Override
    public Mono<Void> deleteBefore(LocalDateTime localDateTime) {
        return jpaRepo.deleteByCreatedTimeBefore(localDateTime);
    }

    @Override
    public Mono<NotificationEntity> saveBatch(List<Long> userIds, Long notifyId, String app, NotificationEntity notificationEntity) {
        return notificationBatchRepo.saveBatch(userIds, notifyId, app, notificationEntity).thenReturn(notificationEntity)
                ;
    }

}

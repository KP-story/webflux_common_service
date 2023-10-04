package com.delight.notify.dao.repo.impl;

import com.delight.gaia.jpa.repo.AbsBaseRepo;
import com.delight.notify.api.model.UserNotification;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationCounterEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import com.delight.notify.dao.repo.UserNotificationCounterRepo;
import com.delight.notify.dao.repo.UserNotificationRepo;
import com.delight.notify.dao.repo.jpa.UserNotificationBatchRepo;
import com.delight.notify.dao.repo.jpa.UserNotificationCounterBatchRepo;
import com.delight.notify.dao.repo.jpa.UserNotificationCounterJpaRepo;
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
public class UserNotificationCounterRepoImpl extends AbsBaseRepo<UserNotificationCounterEntity, Long, UserNotificationCounterJpaRepo> implements UserNotificationCounterRepo {
  private  UserNotificationCounterBatchRepo userNotificationCounterBatchRepo;
    @Override
    public Mono<Integer> countUnreadNotify(Long userId, String app) {
        return jpaRepo.countUnreadNotify(app, userId);
    }

    @Override
    public Mono<Void> resetCounter(Long userId, String app) {
        return jpaRepo.deleteByAppCodeAndUserId(app,userId);
    }

    @Override
    public Mono<Void> increaseCounter(Long userId, String app) {
        return userNotificationCounterBatchRepo.upsetCounter(userId,app);
    }

    @Override
    public Mono<Void> increaseCounter(List<Long> userIds, String app) {
        return userNotificationCounterBatchRepo.saveBatch(userIds,app);
    }


}

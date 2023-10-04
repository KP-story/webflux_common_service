package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.JPABaseRepo;
import com.delight.notify.dao.entity.UserNotificationCounterEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface UserNotificationCounterJpaRepo extends JPABaseRepo<UserNotificationCounterEntity, Long> {

    @Query("select  unread_count from user_notification_counter un  where un.app_code =$1  and un.user_id =$2")
    Mono<Integer> countUnreadNotify(String app, Long userId);

    Mono<Void> deleteByAppCodeAndUserId(String app, Long userId);

}

package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.JPABaseRepo;
import com.delight.notify.dao.entity.UserNotificationEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface UserNotificationJpaRepo extends JPABaseRepo<UserNotificationEntity, Long> {
    @Query("update user_notification set open=true where app_code =$1 and user_id=$3 and notification_id =$2 ")
    @Modifying
    Mono<Void> updateOpen(String app, Long id, Long userId);

    @Query("update user_notification set open=true where app_code =$1 and user_id =$2 ")
    @Modifying
    Mono<Void> updateAllOpen(String app, Long user_id);

    @Query("select  count(1) from user_notification un  where un.app_code =$1  and un.user_id =$2 and un.open =false ")
    Mono<Long> countUnreadNotify(String app, Long userId);

    Mono<Void> deleteByCreatedTimeBefore(LocalDateTime now);

}

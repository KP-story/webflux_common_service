package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.BatchRepo;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import io.r2dbc.spi.Statement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class UserNotificationBatchRepo extends BatchRepo<UserNotificationEntity> {
    public UserNotificationBatchRepo(@Qualifier("notifyEntityTemplate") R2dbcEntityTemplate r2dbcEntityTemplate) throws Exception {
        super(r2dbcEntityTemplate);
    }

    void bind(Statement bind, NotificationEntity notificationEntity) {
        if (notificationEntity.getType() != null) {
            bind.bind(3, notificationEntity.getType());
        } else {
            bind.bindNull(3, String.class);
        }
        if (notificationEntity.getData() != null) {
            bind.bind(4, notificationEntity.getData());
        } else {
            bind.bindNull(4, String.class);
        }
        if (notificationEntity.getCategory() != null) {
            bind.bind(5, notificationEntity.getCategory());
        } else {
            bind.bindNull(5, String.class);
        }
        if (notificationEntity.getScope() != null) {
            bind.bind(6, notificationEntity.getScope().toString());
        } else {
            bind.bindNull(6, String.class);
        }
        if (notificationEntity.getTitle() != null) {
            bind.bind(7, notificationEntity.getTitle());
        } else {
            bind.bindNull(7, String.class);
        }
        if (notificationEntity.getBody() != null) {
            bind.bind(8, notificationEntity.getBody());
        } else {
            bind.bindNull(8, String.class);
        }
        if (notificationEntity.getIcon() != null) {
            bind.bind(9, notificationEntity.getIcon());
        } else {
            bind.bindNull(9, String.class);
        }
        if (notificationEntity.getTitleLocKey() != null) {
            bind.bind(10, notificationEntity.getTitleLocKey());
        } else {
            bind.bindNull(10, String.class);
        }
        if (notificationEntity.getTitleLocArgs() != null) {
            bind.bind(11, notificationEntity.getTitleLocArgs());
        } else {
            bind.bindNull(11, String[].class);
        }
        if (notificationEntity.getBodyLocKey() != null) {
            bind.bind(12, notificationEntity.getBodyLocKey());
        } else {
            bind.bindNull(12, String.class);
        }
        if (notificationEntity.getBodyLocArgs() != null) {
            bind.bind(13, notificationEntity.getBodyLocArgs());
        } else {
            bind.bindNull(13, String[].class);
        }
    }

    public Mono<Void> saveBatch(List<Long> userIds, Long notifyId, String app, NotificationEntity notificationEntity) {

        return databaseClient.inConnection(connection -> {
            var statement = connection.createStatement("INSERT INTO notification.user_notification (user_id, notification_id,  app_code, created_time,type,data,category,scope,title,body,icon,title_loc_key,title_loc_args,body_loc_key,body_loc_args) VALUES($1,  $2,$3, now(),$4,$5,$6,$7,$8,$9,$10,$11,$12,$13,$14) ")
                    .returnGeneratedValues("id");
            Statement bind = statement.bind(0, userIds.get(0)).bind(1, notifyId).bind(2, app);
            bind(bind, notificationEntity);
            for (int i = 1; i < userIds.size(); i++) {
                Statement bind2 = statement.add().bind(0, userIds.get(i)).bind(1, notifyId).bind(2, app);
                bind(bind2, notificationEntity);

            }
            return Flux.from(statement.execute()).flatMap(result -> result.getRowsUpdated()).then();

        }).then();

    }

}

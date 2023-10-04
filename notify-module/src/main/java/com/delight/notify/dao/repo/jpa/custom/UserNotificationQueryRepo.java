package com.delight.notify.dao.repo.jpa.custom;

import com.delight.gaia.jpa.repo.CustomRepo;
import com.delight.notify.api.model.UserNotification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserNotificationQueryRepo extends CustomRepo {
    public UserNotificationQueryRepo(@Qualifier("notifyEntityTemplate") R2dbcEntityTemplate r2dbcEntityTemplate) {
        super(r2dbcEntityTemplate);
    }


    public Flux<UserNotification> loadForwardUserNotify(Long userId, String app, String type, String category, Long firstId, int pageSize) {
        StringBuilder query = new StringBuilder("select * from user_notification un where  un.user_id = $1 and un.app_code = $2  ");
        List<Object> params = new ArrayList<>(7);
        params.add(userId);
        params.add(app);
        if (type != null) {
            params.add(type);
            int index = params.size();
            query.append("and ( un.type = $").append(index).append(" ) ");

        }
        if (firstId != null) {
            params.add(firstId);
            query.append(" and un.notification_id >$").append(params.size());
        }
        if (category != null) {
            params.add(category);
            query.append(" and un.category= $").append(params.size());
        }
        params.add(pageSize);
        query.append("  order by un.notification_id ASC limit  $").append(params.size());
        return query(query.toString(), params, UserNotification.class);
    }

    public Flux<UserNotification> loadBackwardUserNotify(Long userId, String app, String type, String category, Long lastNotifyId, int pageSize) {
        StringBuilder query = new StringBuilder("select * from user_notification un where  un.user_id = $1 and un.app_code = $2 ");
        List<Object> params = new ArrayList<>(7);
        params.add(userId);
        params.add(app);
        if (type != null) {
            params.add(type);
            int index = params.size();
            query.append("and ( un.type = $").append(index).append(" ) ");

        }
        if (lastNotifyId != null) {
            params.add(lastNotifyId);
            query.append(" and un.notification_id < $").append(params.size());
        }
        if (category != null) {
            params.add(category);
            query.append(" and un.category= $").append(params.size());
        }
        params.add(pageSize);
        query.append("  order by un.notification_id DESC limit  $").append(params.size());
        return query(query.toString(), params, UserNotification.class);
    }
}

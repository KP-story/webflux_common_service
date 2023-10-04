package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.BatchRepo;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationCounterEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import io.r2dbc.spi.Statement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class UserNotificationCounterBatchRepo extends BatchRepo<UserNotificationCounterEntity> {
    public UserNotificationCounterBatchRepo(@Qualifier("notifyEntityTemplate") R2dbcEntityTemplate r2dbcEntityTemplate) throws Exception {
        super(r2dbcEntityTemplate);
    }
    private static String UPSET_SQL="INSERT INTO user_notification_counter (app_code, user_id,unread_count ) VALUES($1,$2,0) ON CONFLICT (app_code ,user_id ) DO UPDATE SET unread_count = user_notification_counter.unread_count+1";

    public  Mono<Void> upsetCounter(Long userId, String app)
    {
        return databaseClient.inConnection(connection -> {
            var statement = connection.createStatement(UPSET_SQL);
            statement = getStatement(userId, app, statement);
            return Flux.from(statement.execute()).flatMap(result -> result.getRowsUpdated()).then();
        }).then();
    }

    private Statement getStatement(Long userId, String app, Statement statement) {
        statement =  statement.bind(0, app).bind(1, userId);
        return statement;
    }

    public Mono<Void> saveBatch(List<Long> userIds, String app) {
        return databaseClient.inConnection(connection -> {
                    var statement = connection.createStatement(UPSET_SQL);
                    statement = getStatement(userIds.remove(0), app, statement);
                    for (Long userId : userIds) {
                        getStatement(userId, app, statement.add());
                    }
                    return Flux.from(statement.execute()).flatMap(result -> result.getRowsUpdated()).then();
                }
        ).then();
    }

}

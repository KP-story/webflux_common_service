package com.delight.notify.api.service;

import com.delight.notify.api.model.request.*;
import reactor.core.publisher.Mono;

public interface NotifyService {

    Mono<Long> sendToUsers(UserNotificationRQ userNotificationRQ);

    Mono<Long> sendToUser(SingleUserNotificationRQ userNotificationRQ);

    Mono<Long> sendToUsers(UserNotificationIgnoreDevicesRQ userNotificationRQ);

    Mono<Long> sendToTopic(TopicNotificationRQ topicNotificationRQ);

    Mono<Long> sendToApp(AppNotificationRQ appNotificationRQ);

}

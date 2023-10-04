package com.delight.notify.service.impl;

import com.delight.gaia.auth.context.SecurityUtils;
import com.delight.gaia.base.constant.MessageCode;
import com.delight.gaia.base.exception.CommandFailureException;
import com.delight.notify.api.model.UserNotification;
import com.delight.notify.api.service.UserNotifyService;
import com.delight.notify.dao.repo.UserNotificationCounterRepo;
import com.delight.notify.dao.repo.UserNotificationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserNotifyServiceImpl implements UserNotifyService {
    private final UserNotificationRepo userNotificationRepo;
    private final UserNotificationCounterRepo userNotificationCounterRepo;

    @Override
    public Flux<UserNotification> loadBackwardUserNotification(String type, String category, Long lastNotifyId, int pageSize) {
        return SecurityUtils.getRequester().flatMapMany(subject -> {
            String app = subject.getClientInfo().getApp();
            if (app == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            return userNotificationRepo.loadBackwardUserNotify(subject.getId(), app, type, category, lastNotifyId, pageSize);
        });
    }

    @Override
    public Flux<UserNotification> loadForwardUserNotification(String type, String category, Long firstNotifyId, int pageSize) {
        return SecurityUtils.getRequester().flatMapMany(subject -> {
            String app = subject.getClientInfo().getApp();
            if (app == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            return userNotificationRepo.loadForwardUserNotify(subject.getId(), app, type, category, firstNotifyId, pageSize);
        });
    }


    @Override
    public Mono<Void> markRead(Long notifyId) {
        return SecurityUtils.getRequester().flatMap(subject -> {
            String app = subject.getClientInfo().getApp();
            if (app == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            userNotificationCounterRepo.resetCounter(subject.getId(),app).subscribe();
            return userNotificationRepo.markUnread(subject.getId(), notifyId, app);
        });
    }

    @Override
    public Mono<Void> markReadAll() {
        return SecurityUtils.getRequester().flatMap(subject -> {
            String app = subject.getClientInfo().getApp();
            if (app == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            userNotificationCounterRepo.resetCounter(subject.getId(),app).subscribe();
            return userNotificationRepo.markAllUnread(subject.getId(), app);
        });
    }

    @Override
    public Mono<Void> resetCounter() {
        return SecurityUtils.getRequester().flatMap(subject -> {
            String app = subject.getClientInfo().getApp();
            if (app == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            return userNotificationCounterRepo.resetCounter(subject.getId(), app);
        });
    }

    @Override
    public Mono<Integer> countNotify() {
        return SecurityUtils.getRequester().flatMap(subject -> {
            String app = subject.getClientInfo().getApp();
            if (app == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            return userNotificationCounterRepo.countUnreadNotify(subject.getId(), app).switchIfEmpty(Mono.defer(() -> Mono.just(0)));
        });
    }


}

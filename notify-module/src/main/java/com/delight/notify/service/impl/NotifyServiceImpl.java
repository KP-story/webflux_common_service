package com.delight.notify.service.impl;

import com.delight.gaia.base.exception.CommandFailureException;
import com.delight.notify.api.model.NotificationPayload;
import com.delight.notify.api.model.request.*;
import com.delight.notify.api.service.NotifyService;
import com.delight.notify.cache.AppCache;
import com.delight.notify.constant.AppNotifyType;
import com.delight.notify.constant.NotifyErrorCode;
import com.delight.notify.constant.NotifyScope;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import com.delight.notify.dao.entity.UserTokenEntity;
import com.delight.notify.dao.repo.UserNotificationCounterRepo;
import com.delight.notify.dao.repo.UserNotificationRepo;
import com.delight.notify.dao.repo.UserTokenRepo;
import com.delight.notify.mapper.NotificationMapper;
import com.delight.notify.service.IdGenerator;
import com.delight.notify.service.provider.RemoteResult;
import com.delight.notify.service.provider.notification.NotifyProvider;
import com.delight.notify.service.provider.notification.model.TokenNotifyEnv;
import com.delight.notify.service.provider.notification.model.TopicNotifyEnv;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MessagingErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {
    private static final DateTimeFormatter LOCAL_DATE_TIME_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final NotifyProvider notifyProvider;
    private final UserTokenRepo userTokenRepo;
    private final UserNotificationRepo userNotificationRepo;

    private final UserNotificationCounterRepo userNotificationCounterRepo;
    private final NotificationMapper notificationMapper;
    private final AppCache appCache;
    private final IdGenerator idGeneration;

    private final Long genTransId() {
        return idGeneration.nextId();
    }


    @Transactional("notifyTransactionManager")
    public Mono<NotificationEntity> saveUserNotify(List<Long> userIds, Long notify, String app, NotificationEntity notificationEntity) {
        return userNotificationRepo.saveBatch(userIds, notify, app, notificationEntity).doOnSuccess(notificationEntity1 -> {
            userNotificationCounterRepo.increaseCounter(userIds,app).subscribe();
        });
    }

    @Transactional("notifyTransactionManager")
    public Mono<UserNotificationEntity> saveUserNotify(Long userId, Long notify, String app, UserNotificationEntity userNotificationEntity) {
        userNotificationEntity.setUserId(userId);
        userNotificationEntity.setNotificationId(notify);
        userNotificationEntity.setAppCode(app);
        userNotificationCounterRepo.increaseCounter(userId,app);
        return userNotificationRepo.save(userNotificationEntity);
    }

    public void addExtraInfo(NotificationPayload notificationPayload, Long notifyId,String threadId, String createdTime, boolean isSave) {
        notificationPayload.getData().put("scope", notificationPayload.getScope().name());
        notificationPayload.getData().put("type", notificationPayload.getType());
        notificationPayload.getData().put("category", notificationPayload.getCategory());
        notificationPayload.getData().put("notifyId", notifyId.toString());
        notificationPayload.getData().put("createdTime", createdTime);
        notificationPayload.getData().put("persist", isSave + "");
        if(threadId!=null)
        {
            notificationPayload.getData().put("threadId", threadId);
        }

    }

    @Override
    public Mono<Long> sendToUsers(UserNotificationRQ userNotificationRQ) {
        return sendToUsers(userNotificationRQ, () -> userTokenRepo.findByUser(userNotificationRQ.getUserIds(), userNotificationRQ.getApp()));
    }

    @Override
    public Mono<Long> sendToUser(SingleUserNotificationRQ userNotificationRQ) {
        Long transId = genTransId();
        NotificationPayload notificationPayload = userNotificationRQ.getEnvelope().getPayload();
        notificationPayload.setScope(NotifyScope.USER);
        Mono<Long> sendStream;
        String threadId=null;
        if(userNotificationRQ.getEnvelope().getMessageOptions()!=null)
        {
            threadId=userNotificationRQ.getEnvelope().getMessageOptions().getThreadId();
        }
        if (userNotificationRQ.isSave()) {
            UserNotificationEntity notificationEntity = notificationMapper.userNotifyToEntity(notificationPayload);
            String finalThreadId = threadId;
            sendStream = saveUserNotify(userNotificationRQ.getUserId(), transId, userNotificationRQ.getApp(), notificationEntity)
                    .map(userNotificationEntity -> {
                        addExtraInfo(notificationPayload, userNotificationEntity.getNotificationId(), finalThreadId, userNotificationEntity.getCreatedTime().format(LOCAL_DATE_TIME_PATTERN), userNotificationRQ.isSave());
                        return transId;
                    });
        } else {
            addExtraInfo(notificationPayload,userNotificationRQ.getCustomId()==null? transId:userNotificationRQ.getCustomId(),threadId,LocalDateTime.now().format(LOCAL_DATE_TIME_PATTERN),false);
            sendStream = Mono.just(transId);
        }
        return sendStream.doOnSuccess(l -> {
            userTokenRepo.findByUser(userNotificationRQ.getUserId(), userNotificationRQ.getApp()).collectList().flatMap(userTokenEntities ->
                    {
                        if (userTokenEntities.isEmpty())
                            return Mono.just(transId);
                        TokenNotifyEnv tokenNotifyEnv = new TokenNotifyEnv().setTokens(userTokenEntities.stream().map(UserTokenEntity::getToken).collect(Collectors.toList()));
                        tokenNotifyEnv.setPayload(notificationPayload).setMessageOptions(userNotificationRQ.getEnvelope().getMessageOptions());
                        return notifyProvider.sendToTokens(userNotificationRQ.getApp(), tokenNotifyEnv).flatMap(notifyResults -> {
                            List<Long> invalidTokens = new ArrayList<>(notifyResults.size());
                            for (int i = 0; i < notifyResults.size(); i++) {
                                RemoteResult notifyResult = notifyResults.get(i);
                                if (!notifyResult.isSuccess()) {
                                    Exception exception = notifyResult.getException();
                                    if (exception instanceof FirebaseMessagingException) {
                                        FirebaseMessagingException firebaseMessagingException = (FirebaseMessagingException) exception;
                                        if (firebaseMessagingException.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                                            invalidTokens.add(userTokenEntities.get(i).getId());
                                        }
                                    }
                                }
                            }
                            if (invalidTokens.isEmpty()) {
                                return Mono.just(transId);
                            }
                            return userTokenRepo.deleteByTokenIds(invalidTokens, userNotificationRQ.getApp()).thenReturn(transId);
                        });
                    }
            ).subscribe();
        });
    }

    public Mono<Long> sendToUsers(UserNotificationRQ userNotificationRQ, Supplier<Flux<UserTokenEntity>> getTokens) {
        Long transId = genTransId();
        NotificationPayload notificationPayload = userNotificationRQ.getEnvelope().getPayload();
        notificationPayload.setScope(NotifyScope.USER);
        Mono<Long> sendStream;
        String threadId=null;
        if(userNotificationRQ.getEnvelope().getMessageOptions()!=null)
        {
            threadId=userNotificationRQ.getEnvelope().getMessageOptions().getThreadId();
        }
        if (userNotificationRQ.isSave()) {
            String finalThreadId = threadId;
            sendStream = saveUserNotify(userNotificationRQ.getUserIds(), transId, userNotificationRQ.getApp(), notificationMapper.notifyToEntity(notificationPayload))
                    .map(notificationEntity -> {
                        addExtraInfo(notificationPayload, transId, finalThreadId, LocalDateTime.now().format(LOCAL_DATE_TIME_PATTERN), userNotificationRQ.isSave());
                        return transId;
                    });
        } else {
            addExtraInfo(notificationPayload,userNotificationRQ.getCustomId()==null? transId:userNotificationRQ.getCustomId(),threadId,LocalDateTime.now().format(LOCAL_DATE_TIME_PATTERN),false);
            sendStream = Mono.just(transId);
        }

        return sendStream.doOnSuccess(l -> {
            getTokens.get().collectList().flatMap(userTokenEntities ->
                    {
                        if (userTokenEntities.isEmpty())
                            return Mono.just(transId);

                        TokenNotifyEnv tokenNotifyEnv = new TokenNotifyEnv().setTokens(userTokenEntities.stream().map(UserTokenEntity::getToken).collect(Collectors.toList()));
                        tokenNotifyEnv.setPayload(notificationPayload).setMessageOptions(userNotificationRQ.getEnvelope().getMessageOptions());

                        return notifyProvider.sendToTokens(userNotificationRQ.getApp(), tokenNotifyEnv).flatMap(notifyResults -> {
                            List<Long> invalidTokens = new ArrayList<>(notifyResults.size());
                            for (int i = 0; i < notifyResults.size(); i++) {
                                RemoteResult notifyResult = notifyResults.get(i);
                                if (!notifyResult.isSuccess()) {
                                    Exception exception = notifyResult.getException();
                                    if (exception instanceof FirebaseMessagingException) {
                                        FirebaseMessagingException firebaseMessagingException = (FirebaseMessagingException) exception;
                                        if (firebaseMessagingException.getMessagingErrorCode().equals(MessagingErrorCode.UNREGISTERED)) {
                                            invalidTokens.add(userTokenEntities.get(i).getId());
                                        }
                                    }
                                }
                            }
                            if (invalidTokens.isEmpty()) {
                                return Mono.just(transId);
                            }
                            return userTokenRepo.deleteByTokenIds(invalidTokens, userNotificationRQ.getApp()).thenReturn(transId);
                        });
                    }
            ).subscribe();
        });
    }

    @Override
    public Mono<Long> sendToUsers(UserNotificationIgnoreDevicesRQ userNotificationRQ) {
        return sendToUsers(userNotificationRQ, () -> userTokenRepo.findByUserIgnoreDevices(userNotificationRQ.getUserIds(), userNotificationRQ.getApp(), userNotificationRQ.getIgnoreDeviceIds()));
    }

    @Override
    public Mono<Long> sendToTopic(TopicNotificationRQ topicNotificationRQ) {
        Long transId = genTransId();
        TopicNotifyEnv topicNotifyEnv = new TopicNotifyEnv().setTopics(topicNotificationRQ.getTopics());
        topicNotifyEnv.setPayload(topicNotificationRQ.getEnvelope().getPayload()).setMessageOptions(topicNotificationRQ.getEnvelope().getMessageOptions());

        String threadId=null;
        if(topicNotificationRQ.getEnvelope().getMessageOptions()!=null)
        {
            threadId=topicNotificationRQ.getEnvelope().getMessageOptions().getThreadId();
        }
        addExtraInfo(topicNotificationRQ.getEnvelope().getPayload(), topicNotificationRQ.getCustomId()==null? transId:topicNotificationRQ.getCustomId(),threadId, LocalDateTime.now().format(LOCAL_DATE_TIME_PATTERN), false);
        return notifyProvider.sendToTopic(topicNotificationRQ.getApp(), topicNotifyEnv).thenReturn(transId);
    }

    @Override
    public Mono<Long> sendToApp(AppNotificationRQ appNotificationRQ) {
        Long transId = genTransId();
        return appCache.get(appNotificationRQ.getApp()).switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(NotifyErrorCode.NOTIFY_APP_NOTFOUND)))).flatMap(appInfo -> {
            String topic;
            if (appNotificationRQ.getSendType().equals(AppNotifyType.ONLY_NORMAL_USER)) {
                topic = appInfo.getLoginTopic();
            } else {
                topic = appInfo.getTopic();
            }
            TopicNotifyEnv topicNotifyEnv = new TopicNotifyEnv();
            topicNotifyEnv.setTopics(List.of(topic));
            topicNotifyEnv.setPayload(appNotificationRQ.getEnvelope().getPayload());
            topicNotifyEnv.setMessageOptions(appNotificationRQ.getEnvelope().getMessageOptions());
            String threadId=null;
            if(appNotificationRQ.getEnvelope().getMessageOptions()!=null)
            {
                threadId=appNotificationRQ.getEnvelope().getMessageOptions().getThreadId();
            }
            addExtraInfo(appNotificationRQ.getEnvelope().getPayload(), appNotificationRQ.getCustomId()==null? transId:appNotificationRQ.getCustomId(),threadId, LocalDateTime.now().format(LOCAL_DATE_TIME_PATTERN), false);
            return notifyProvider.sendToTopic(appInfo.getCode(), topicNotifyEnv);
        }).map(notifyResults -> transId);
    }

}

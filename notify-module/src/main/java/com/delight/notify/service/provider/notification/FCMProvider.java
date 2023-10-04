package com.delight.notify.service.provider.notification;

import com.delight.gaia.base.vo.Pair;
import com.delight.notify.api.model.MessageOptions;
import com.delight.notify.api.model.NotificationPayload;
import com.delight.notify.cache.AppCache;
import com.delight.notify.service.provider.RemoteResult;
import com.delight.notify.service.provider.notification.model.TokenNotifyEnv;
import com.delight.notify.service.provider.notification.model.TopicNotifyEnv;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FCMProvider implements NotifyProvider {
    private final AppCache appCache;
    AsyncCache<String, FirebaseApp> firebaseMessagingMap = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.HOURS).expireAfterAccess(5, TimeUnit.HOURS).scheduler(Scheduler.systemScheduler())
            .removalListener((key, value, cause) -> {
                FirebaseApp firebaseApp = (FirebaseApp) value;
                firebaseApp.delete();
            }).evictionListener((key, value, cause) ->
            {
                FirebaseApp firebaseApp = (FirebaseApp) value;
                firebaseApp.delete();
            })
            .maximumSize(100)
            .buildAsync();

    private Mono<FirebaseMessaging> getFirebase(String app) {
        return Mono.fromFuture(firebaseMessagingMap.get(app, (s, executor) -> appCache.get(app).map(appEntity -> {
            InputStream cred = new ByteArrayInputStream(appEntity.getKey().getBytes());
            try {

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(cred))
                        .build();

                return FirebaseApp.initializeApp(options, app);

            } catch (Exception e) {
                throw Exceptions.propagate(e);
            }
        }).toFuture())).map(FirebaseMessaging::getInstance);
    }


    @Override
    public Mono<RemoteResult> registerToken(String app, String token, String topic) {
        return getFirebase(app).handle((firebaseMessaging, synchronousSink) -> {
            try {
                TopicManagementResponse res = firebaseMessaging.subscribeToTopic(Arrays.asList(token), topic);
                synchronousSink.next(ResultMapper.map(res));
            } catch (Exception e) {
                synchronousSink.error(e);
            }
        });
    }

    @Override
    public Mono<RemoteResult> unregisterToken(String app, String token, String topic) {
        return getFirebase(app).handle((firebaseMessaging, synchronousSink) -> {
            try {
                TopicManagementResponse res = firebaseMessaging.unsubscribeFromTopic(Arrays.asList(token), topic);
                synchronousSink.next(ResultMapper.map(res));
            } catch (Exception e) {
                synchronousSink.error(e);
            }
        });
    }

    @Override
    public Mono<RemoteResult> registerToken(String app, List<String> tokens, String topic) {
        return getFirebase(app).handle((firebaseMessaging, synchronousSink) -> {
            try {
                TopicManagementResponse res = firebaseMessaging.subscribeToTopic(tokens, topic);
                synchronousSink.next(ResultMapper.map(res));
            } catch (Exception e) {
                synchronousSink.error(e);
            }
        });
    }

    @Override
    public Mono<RemoteResult> unregisterToken(String app, List<String> tokens, String topic) {
        return getFirebase(app).handle((firebaseMessaging, synchronousSink) -> {
            try {
                TopicManagementResponse res = firebaseMessaging.unsubscribeFromTopic(tokens, topic);
                synchronousSink.next(ResultMapper.map(res));
            } catch (Exception e) {
                synchronousSink.error(e);
            }
        });
    }

    @Override
    public Mono<List<RemoteResult>> sendToTopic(String app, TopicNotifyEnv topicNotifyEnv) {
        List<Message> messages = buildMessage(topicNotifyEnv);
        return getFirebase(app).handle((firebaseMessaging, synchronousSink) -> {
            try {
                BatchResponse batchResponse = firebaseMessaging.sendAll(messages);
                synchronousSink.next(ResultMapper.map(batchResponse));
            } catch (FirebaseMessagingException e) {
                synchronousSink.error(e);
            }
        });
    }


    @Override
    public Mono<List<RemoteResult>> sendToTokens(String app, TokenNotifyEnv tokenNotifyEnvs) {
        return getFirebase(app).handle((firebaseMessaging, synchronousSink) -> {
            try {
                MulticastMessage multicastMessage = buildMulticastMessage(tokenNotifyEnvs);
                BatchResponse batchResponse = firebaseMessaging.sendMulticast(multicastMessage);
                synchronousSink.next(ResultMapper.map(batchResponse));
            } catch (FirebaseMessagingException e) {
                synchronousSink.error(e);
            }
        });
    }

    protected List<Message> buildListTopicMessage(List<TopicNotifyEnv> topicNotifyEnvs) {
        return topicNotifyEnvs.stream().flatMap(topicNotifyEnv -> buildMessage(topicNotifyEnv).stream()).collect(Collectors.toList());
    }

    protected List<MulticastMessage> buildListMulticastMessage(List<TokenNotifyEnv> tokenNotifyEnvs) {
        return tokenNotifyEnvs.stream().map(this::buildMulticastMessage).collect(Collectors.toList());
    }

    protected MulticastMessage buildMulticastMessage(TokenNotifyEnv tokenNotifyEnv) {
        MulticastMessage.Builder message = MulticastMessage.builder()
                .addAllTokens(tokenNotifyEnv.getTokens());
        if (tokenNotifyEnv.getPayload().getData() != null) {
            message.putAllData(tokenNotifyEnv.getPayload().getData());
        }
        message = buildOptions(tokenNotifyEnv.getMessageOptions(), tokenNotifyEnv.getPayload(), message);
        return message.build();
    }


    protected List<Message> buildMessage(TopicNotifyEnv topicNotifyEnv) {
        List<Message> messages = new ArrayList<>(topicNotifyEnv.getTopics().size());
        for (String topic : topicNotifyEnv.getTopics()) {
            Message.Builder message = Message.builder().putAllData(topicNotifyEnv.getPayload().getData()).setTopic(topic);
            message = buildOptions(topicNotifyEnv.getMessageOptions(), topicNotifyEnv.getPayload(), message);
            messages.add(message.build());
        }
        return messages;
    }

    Pair<AndroidNotification, ApsAlert> buildLocalizeNotification(NotificationPayload notificationPayload) {
        AndroidNotification.Builder androidNotificationBuilder = AndroidNotification.builder();
        ApsAlert.Builder apsAlert = ApsAlert.builder();
        androidNotificationBuilder.setTitleLocalizationKey(notificationPayload.getTitleLocKey());
        apsAlert.setTitleLocalizationKey(notificationPayload.getTitleLocKey());
        if (notificationPayload.getTitleLocArgs() != null) {
            androidNotificationBuilder.addAllTitleLocalizationArgs(notificationPayload.getTitleLocArgs());
            apsAlert.addAllTitleLocArgs(notificationPayload.getTitleLocArgs());
        }
        if (notificationPayload.getBodyLocKey() != null) {
            androidNotificationBuilder.setBodyLocalizationKey(notificationPayload.getBodyLocKey());
            apsAlert.setLocalizationKey(notificationPayload.getBodyLocKey());
            if (notificationPayload.getBodyLocArgs() != null) {
                {
                    androidNotificationBuilder.addAllBodyLocalizationArgs(notificationPayload.getBodyLocArgs());
                    apsAlert.addAllLocalizationArgs(notificationPayload.getBodyLocArgs());

                }
            }

        } else {
            androidNotificationBuilder.setBody(notificationPayload.getBody());
            apsAlert.setBody(notificationPayload.getBody());
        }
        return new Pair<AndroidNotification, ApsAlert>().setLeft(androidNotificationBuilder.build()).setRight(apsAlert.build());
    }

    public MulticastMessage.Builder buildOptions(MessageOptions messageOptions, NotificationPayload notificationPayload, MulticastMessage.Builder messageBuilder) {
        AndroidConfig.Builder androidConfigBuilder = null;
        ApnsConfig.Builder apnConfigBuilder = null;
        ApsAlert alert = null;
        if (notificationPayload != null) {
            if (notificationPayload.getTitleLocKey() == null) {
                var no = com.google.firebase.messaging.Notification.builder().setTitle(notificationPayload.getTitle()).setBody(notificationPayload.getBody());
                if (notificationPayload.getIcon() != null) {
                    no.setImage(notificationPayload.getIcon());
                }
                messageBuilder.setNotification(no.build());
            } else {
                androidConfigBuilder = AndroidConfig.builder();
                var pair = buildLocalizeNotification(notificationPayload);
                androidConfigBuilder.setNotification(pair.getLeft());
                alert = pair.getRight();
            }

        }
        if (messageOptions != null) {
            if (androidConfigBuilder == null) ;
            {
                androidConfigBuilder = AndroidConfig.builder();
            }
            if (messageOptions.isContentAvailable()) {
                androidConfigBuilder.setPriority(AndroidConfig.Priority.HIGH);
            }

            Aps.Builder aps = Aps.builder();
            if (alert != null) {
                aps.setAlert(alert);
            }
            apnConfigBuilder = ApnsConfig.builder();
            aps.setMutableContent(messageOptions.isMutableContent())
                    .setContentAvailable(messageOptions.isContentAvailable());
            if(messageOptions.getThreadId()!=null)
            {
                aps.setThreadId(messageOptions.getThreadId());
            }
            apnConfigBuilder.setAps(aps.build());
        }
        if (androidConfigBuilder != null)
            messageBuilder.setAndroidConfig(androidConfigBuilder.build());
        if (apnConfigBuilder == null && alert != null) {
            apnConfigBuilder = ApnsConfig.builder();
            apnConfigBuilder.setAps(Aps.builder().setAlert(alert).build());
        }
        if (apnConfigBuilder != null) {
            messageBuilder.setApnsConfig(apnConfigBuilder.build());
        }
        return messageBuilder;
    }

    public Message.Builder buildOptions(MessageOptions messageOptions, NotificationPayload notificationPayload, Message.Builder messageBuilder) {
        AndroidConfig.Builder androidConfigBuilder = null;
        ApnsConfig.Builder apnConfigBuilder = null;
        ApsAlert alert = null;
        if (notificationPayload != null) {
            if (notificationPayload.getTitleLocKey() == null) {
                var no = com.google.firebase.messaging.Notification.builder().setTitle(notificationPayload.getTitle()).setBody(notificationPayload.getBody());
                if (notificationPayload.getIcon() != null) {
                    no.setImage(notificationPayload.getIcon());
                }
                messageBuilder.setNotification(no.build());
            } else {
                androidConfigBuilder = AndroidConfig.builder();
                var pair = buildLocalizeNotification(notificationPayload);
                androidConfigBuilder.setNotification(pair.getLeft());
                alert = pair.getRight();
            }

        }
        if (messageOptions != null) {
            if (androidConfigBuilder == null) ;
            {
                androidConfigBuilder = AndroidConfig.builder();
            }
            if (messageOptions.isContentAvailable()) {

                androidConfigBuilder.setPriority(AndroidConfig.Priority.HIGH);
            }
            Aps.Builder aps = Aps.builder();
            if (alert != null) {
                aps.setAlert(alert);
            }
            apnConfigBuilder = ApnsConfig.builder();
            aps.setMutableContent(messageOptions.isMutableContent()).setContentAvailable(messageOptions.isContentAvailable());
            apnConfigBuilder.setAps(aps.build());
            if(messageOptions.getThreadId()!=null)
            {
                aps.setThreadId(messageOptions.getThreadId());
            }
        }
        if (androidConfigBuilder != null)
            messageBuilder.setAndroidConfig(androidConfigBuilder.build());
        if (apnConfigBuilder == null && alert != null) {
            apnConfigBuilder = ApnsConfig.builder();
            apnConfigBuilder.setAps(Aps.builder().setAlert(alert).build());
        }

        if (apnConfigBuilder != null) {
            messageBuilder.setApnsConfig(apnConfigBuilder.build());
        }
        return messageBuilder;
    }


}

package com.delight.notify.service.provider.notification;

import com.delight.notify.service.provider.RemoteResult;
import com.delight.notify.service.provider.notification.model.TokenNotifyEnv;
import com.delight.notify.service.provider.notification.model.TopicNotifyEnv;
import reactor.core.publisher.Mono;

import java.util.List;

public interface NotifyProvider {

    Mono<RemoteResult> registerToken(String app, String token, String topic);

    Mono<RemoteResult> unregisterToken(String app, String token, String topic);

    Mono<RemoteResult> registerToken(String app, List<String> tokens, String topic);

    Mono<RemoteResult> unregisterToken(String app, List<String> tokens, String topic);

    Mono<List<RemoteResult>> sendToTopic(String app, TopicNotifyEnv topicNotifyEnvs);

    Mono<List<RemoteResult>> sendToTokens(String app, TokenNotifyEnv tokenNotifyEnvs);

}

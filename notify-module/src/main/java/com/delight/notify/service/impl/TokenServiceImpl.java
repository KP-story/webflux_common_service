package com.delight.notify.service.impl;

import com.delight.gaia.auth.context.SecurityUtils;
import com.delight.gaia.auth.subject.Subject;
import com.delight.gaia.base.constant.AccountType;
import com.delight.gaia.base.constant.MessageCode;
import com.delight.gaia.base.constant.Platform;
import com.delight.gaia.base.exception.CommandFailureException;
import com.delight.notify.api.service.TokenService;
import com.delight.notify.cache.AppCache;
import com.delight.notify.constant.NotifyErrorCode;
import com.delight.notify.dao.entity.UserTokenEntity;
import com.delight.notify.dao.repo.UserTokenRepo;
import com.delight.notify.model.AppInfo;
import com.delight.notify.service.provider.RemoteResult;
import com.delight.notify.service.provider.notification.NotifyProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.delight.notify.constant.NotifyErrorCode.TOKEN_INVALID;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final NotifyProvider notifyProvider;
    private final UserTokenRepo userTokenRepo;
    private final AppCache appCache;
    @Value("${auth.maxSession}")
    private Short maxSession;

    @PostConstruct
    void init() {
        maxSession--;
    }

    @Override
    public Mono<Void> registerToken(String token) {
        return SecurityUtils.getRequester().flatMap(subject -> {
            String appCode = subject.getClientInfo().getApp();
            if (appCode == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header app"));
            }
            Platform platform = subject.getClientInfo().getPlatform();
            if (platform == null) {
                return Mono.error(new CommandFailureException(MessageCode.MISSING_PARAMETER, "header platform"));
            }
            return appCache.get(appCode).switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(NotifyErrorCode.NOTIFY_APP_NOTFOUND))))
                    .doOnSuccess(appInfo -> {
                        if (subject.getInfo().getAccountType().equals(AccountType.NORMAL)) {
                            userTokenRepo.deleteExceedToken(subject.getId(), token, appInfo.getCode(), maxSession, subject.getClientInfo().getDeviceId()).filter(expiredToken -> !expiredToken.equals(token)).collectList().flatMap(tokens ->
                                    unregisterTokens(tokens, appInfo, appCode)).flatMap(__ -> register(subject, token, appInfo, platform)).subscribe();
                        } else
                            userTokenRepo.deleteToken(token, appCode).flatMap(__ -> register(subject, token, appInfo, platform)).subscribe();

                    }).then();
        });
    }

    @Override
    public Mono<Void> unregisterDeviceIds(List<String> deviceIds, Long userId, String app) {
        return appCache.get(app).switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(NotifyErrorCode.NOTIFY_APP_NOTFOUND))))
                .flatMap(appInfo ->
                        userTokenRepo.findByUserAndDeviceIds(userId, deviceIds, app).collectList().flatMap(userTokenEntities -> {
                            if (userTokenEntities.isEmpty()) {
                                return Mono.just(true).then();
                            }
                            Mono<RemoteResult> registerMono;
                            Mono<RemoteResult> unregisterMono;
                            List<Long> ids = new ArrayList<>(deviceIds.size());
                            List<String> tokens = new ArrayList<>(deviceIds.size());
                            for (UserTokenEntity userTokenEntity : userTokenEntities) {
                                ids.add(userTokenEntity.getId());
                                tokens.add(userTokenEntity.getToken());
                            }
                            registerMono = notifyProvider.unregisterToken(app, tokens, appInfo.getLoginTopic());
                            unregisterMono = notifyProvider.registerToken(app, tokens, appInfo.getTopic());
                            return Mono.zip(registerMono.subscribeOn(Schedulers.boundedElastic()), unregisterMono.subscribeOn(Schedulers.boundedElastic()), userTokenRepo.deleteByTokenIds(ids, app)).then();
                        }));
    }

    public Mono unregisterTokens(List<String> tokens, AppInfo appInfo, String app) {
        if (tokens.isEmpty()) {
            return Mono.just(true);
        }
        Mono<RemoteResult> registerMono;
        Mono<RemoteResult> unregisterMono;
        registerMono = notifyProvider.unregisterToken(app, tokens, appInfo.getLoginTopic());
        unregisterMono = notifyProvider.registerToken(app, tokens, appInfo.getTopic());
        return Mono.zip(registerMono.subscribeOn(Schedulers.boundedElastic()), unregisterMono.subscribeOn(Schedulers.boundedElastic()));
    }


    Mono register(Subject subject, String token, AppInfo appInfo, Platform platform) {
        Mono<RemoteResult> registerMono;
        Mono<RemoteResult> unregisterMono;
        String appCode = appInfo.getCode();
        if (subject.getInfo().getAccountType().equals(AccountType.ANONYMOUS)) {
            registerMono = notifyProvider.registerToken(appCode, token, appInfo.getTopic());
            unregisterMono = notifyProvider.unregisterToken(appCode, token, appInfo.getLoginTopic());

        } else {
            registerMono = notifyProvider.registerToken(appCode, token, appInfo.getTopic());
            unregisterMono = notifyProvider.registerToken(appCode, token, appInfo.getLoginTopic());
        }
        return Mono.zip(registerMono.subscribeOn(Schedulers.boundedElastic()), unregisterMono.subscribeOn(Schedulers.boundedElastic())).flatMap(objects -> {
            RemoteResult registerRs = objects.getT1();
            RemoteResult unregisterRs = objects.getT2();
            if (!unregisterRs.isSuccess()) {
                log.error("unregister token {} reason {} failed ", token, unregisterRs.getReason(), unregisterRs.getException());
            }
            if (!registerRs.isSuccess()) {
                log.error("register token {} reason {} failed ", token, registerRs.getReason(), registerRs.getException());
                return Mono.error(new CommandFailureException(TOKEN_INVALID, registerRs.getReason()));
            } else {
                if (subject.getInfo().getAccountType().equals(AccountType.ANONYMOUS)) {
                    return Mono.empty();
                }
                UserTokenEntity userTokenEntity = new UserTokenEntity();
                userTokenEntity.setToken(token);
                userTokenEntity.setUserId(subject.getId());
                userTokenEntity.setAppCode(appCode);
                userTokenEntity.setAppId(appInfo.getId());
                userTokenEntity.setDeviceId(subject.getClientInfo().getDeviceId());
                userTokenEntity.setPlatformCode(platform);
                return userTokenRepo.save(userTokenEntity).then();
            }
        });
    }

}

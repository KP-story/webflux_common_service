package com.delight.assets.service.impl;

import com.delight.gaia.asset.AssetCommon;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AssetCommonCache implements AssetCommon {
    private final FileServiceImpl fileService;
    @Value("${account.avatarAsset}")
    private String accountAvatarConfig;
    @Value("${chat.avatarAsset}")
    private String chatAvatarConfig;
    private String accountPrefixUrl;
    private String channelPrefixUrl;

    @PostConstruct
    void init() {
        getAccountAvatar().subscribe();
    }

    @Override
    public Mono<String> getPrefixUrl(String s) {
        return null;
    }

    @Override
    public Mono<String> getAccountAvatar() {
        if (accountPrefixUrl == null) {
            return fileService.getUrlPrefix(accountAvatarConfig).doOnSuccess(s -> {
                accountPrefixUrl = s;
            });
        }
        return Mono.just(accountPrefixUrl);
    }

    @Override
    public String getAccountAvatarSync() {
        return accountPrefixUrl;
    }

    @Override
    public Mono<String> getChatChannelAvatar() {
        if (channelPrefixUrl == null) {
            return fileService.getUrlPrefix(chatAvatarConfig).doOnSuccess(s -> {
                channelPrefixUrl = s;
            });
        }
        return Mono.just(channelPrefixUrl);    }

    @Override
    public String getChatChannelAvatarSync() {
        return channelPrefixUrl;
    }


    @Override
    public Mono<Map<String, String>> getPrefixUrls(List<String> list) {
        return null;
    }
}

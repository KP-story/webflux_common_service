package com.delight.auth.service.impl.oauth;

import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.constant.OAuthType;
import com.delight.auth.external.GoogleOAuthClient;
import com.delight.auth.external.dto.GAccountInfo;
import com.delight.auth.mapper.OAuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class GoogleProvider implements OAuthProvider {
    private final GoogleOAuthClient googleOAuthClient;
    private final OAuthMapper oAuthMapper;

    @Override
    @SneakyThrows
    public Mono<OAuthIdentity> verifyOAuth(OAuthRQ oAuthRQ) {
        Mono<GAccountInfo> call = googleOAuthClient.getGoogleAccountInfo(oAuthRQ.getAccessToken());
        return call.map(oAuthMapper::getOAuthIdentityFromGGAccountInfo);
    }


    @Override
    public OAuthType getType() {
        return OAuthType.GOOGLE;
    }
}

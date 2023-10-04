package com.delight.auth.service.impl.oauth;

import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.constant.OAuthType;
import com.delight.auth.external.FbClient;
import com.delight.auth.external.dto.FAccountInfo;
import com.delight.auth.mapper.OAuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class FacebookProvider implements OAuthProvider {
    public static final String OAUTH_FIELDS = "id,name,picture,first_name,middle_name,last_name,link,gender,email";

    private final FbClient fbClient;
    private final OAuthMapper oAuthMapper;

    @Override
    @SneakyThrows
    public Mono<OAuthIdentity> verifyOAuth(OAuthRQ oAuthRQ) {
        Mono<FAccountInfo> call = fbClient.getAccountInfo(oAuthRQ.getAccessToken(), OAUTH_FIELDS);
        return call.map(oAuthMapper::getOAuthIdentityFromFbAccountInfo).map(oAuthIdentity -> {
            if (oAuthIdentity.getEmail() == null || oAuthIdentity.getEmail().length() < 2) {
                oAuthIdentity.setEmail(oAuthIdentity.getId() + "@facebook.com");
            }
            return oAuthIdentity;
        });
    }

    @Override
    public OAuthType getType() {
        return OAuthType.FACEBOOK;
    }

}

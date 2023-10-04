package com.delight.auth.service.impl.oauth;

import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.constant.OAuthType;
import reactor.core.publisher.Mono;

public interface OAuthProvider {
    Mono<OAuthIdentity> verifyOAuth(OAuthRQ oAuthRQ);

    OAuthType getType();
}

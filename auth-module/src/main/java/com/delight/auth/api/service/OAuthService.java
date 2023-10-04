package com.delight.auth.api.service;


import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.api.model.response.LoginRS;
import reactor.core.publisher.Mono;

public interface OAuthService {

    Mono<LoginRS> loginUserOAuth(OAuthRQ oAuthRQ, String deviceId, String platform, String deviceName, String deviceOs, String appName, String appVersion, Double latitude, Double longitude);

}

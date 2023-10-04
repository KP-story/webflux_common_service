package com.delight.auth.api.service;

import com.delight.auth.api.model.UserSimpleInfo;
import com.delight.auth.api.model.request.LoginRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.model.response.TokenRS;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<LoginRS> login(LoginRQ loginRQ, String deviceId, String platform, String deviceName, String deviceOs, String app, String appVersion, Double latitude, Double longitude);

    Mono<Void> logout(String deviceId);

    Mono<TokenRS> refreshToken(String deviceId, String refreshToken, String app);

    Mono<LoginRS> handleLogin(UserSimpleInfo user, String deviceId, String platform, String deviceName, String deviceOs, String appName, String appVersion, Double latitude, Double longitude);

}

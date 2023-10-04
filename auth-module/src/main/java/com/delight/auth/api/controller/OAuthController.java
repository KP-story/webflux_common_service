package com.delight.auth.api.controller;

import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.service.OAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class OAuthController {
    private OAuthService oAuthService;


    @PostMapping("/oauth")
    public Mono<LoginRS> loginV2(@RequestBody @Valid OAuthRQ rq, @RequestHeader(value = "device-id") String deviceId, @RequestHeader(value = "device-name", required = false) String deviceName,
                                 @RequestHeader(value = "device-os", required = false) String deviceOs, @RequestHeader(value = "app", required = false) String app,
                                 @RequestHeader(value = "app-version", required = false) String appVersion, @RequestHeader(value = "latitude", required = false) Double latitude,
                                 @RequestHeader(value = "longitude", required = false) Double longitude, @RequestHeader(value = "platform") String platform
    ) {

        return oAuthService.loginUserOAuth(rq, deviceId, platform, deviceName, deviceOs, app, appVersion, latitude, longitude);

    }
}

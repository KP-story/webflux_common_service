package com.delight.auth.api.controller;

import com.delight.auth.api.model.request.LoginRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.model.response.TokenRS;
import com.delight.auth.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public Mono<LoginRS> loginV2(@RequestBody @Valid LoginRQ loginRQ, @RequestHeader("device-id") String deviceId, @RequestHeader(value = "device-name", required = false) String deviceName,
                                 @RequestHeader(value = "device-os", required = false) String deviceOs, @RequestHeader(value = "app", required = false) String appName,
                                 @RequestHeader(value = "app-version", required = false) String appVersion, @RequestHeader(value = "latitude", required = false) Double latitude,
                                 @RequestHeader(value = "longitude", required = false) Double longitude, @RequestHeader(value = "platform") String platform) {
        return authService.login(loginRQ, deviceId, platform, deviceName, deviceOs, appName, appVersion, latitude, longitude);
    }

    @DeleteMapping("/logout")
    public Mono<Void> logout(@RequestHeader(value = "device-id", required = false) String deviceId) {
        return authService.logout(deviceId);
    }

    @GetMapping("/refresh-token")
    public Mono<TokenRS> refreshIdTokenV2(@RequestHeader("device-id") String deviceId, @RequestParam String refreshToken, @RequestHeader(value = "app") String appName) {
        return authService.refreshToken(deviceId, refreshToken, appName);
    }
}

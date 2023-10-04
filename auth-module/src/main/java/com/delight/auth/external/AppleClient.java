package com.delight.auth.external;

import com.delight.auth.external.dto.AppleTokenResponse;
import com.delight.auth.external.dto.JwkJsons;
import com.delight.gaia.connector.http.HttpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import reactor.core.publisher.Mono;

@HttpClient("apple-oauth-client")
public interface AppleClient {
    @PostMapping("token")
    Mono<AppleTokenResponse> getToken(@RequestPart("client_id") String appClientId, @RequestPart("client_secret") String clientSecret, @RequestPart("grant_type") String grantType, @RequestPart("code") String authCode);

    @GetMapping("keys")
    Mono<JwkJsons> getPublicKeysFromApple();
}

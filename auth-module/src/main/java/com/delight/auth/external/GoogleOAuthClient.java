package com.delight.auth.external;

import com.delight.auth.external.dto.GAccountInfo;
import com.delight.gaia.connector.http.HttpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@HttpClient("gg-oauth-client")
public interface GoogleOAuthClient {
    @GetMapping("tokeninfo")
    Mono<GAccountInfo> getGoogleAccountInfo(@RequestParam("id_token") String accessToken);
}

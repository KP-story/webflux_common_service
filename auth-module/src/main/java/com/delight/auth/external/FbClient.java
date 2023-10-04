package com.delight.auth.external;

import com.delight.auth.external.dto.FAccountInfo;
import com.delight.gaia.connector.http.HttpClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@HttpClient("fb-client")
public interface FbClient {
    @GetMapping("me")
    Mono<FAccountInfo> getAccountInfo(@RequestParam("access_token") String accessToken, @RequestParam("fields") String fields);
}

package com.delight.auth.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequestMapping("/auth/health-check")
@RestController
@AllArgsConstructor
public class HealthCheckController {

    @PostMapping("")
    public Mono<String> healthCheck(@RequestPart String name) {
        return Mono.just("hi " + name + ",i am fine");
    }
}

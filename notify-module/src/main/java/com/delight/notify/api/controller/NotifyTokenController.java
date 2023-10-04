package com.delight.notify.api.controller;

import com.delight.notify.api.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RequestMapping("/notify/token")
@RestController
@AllArgsConstructor
public class NotifyTokenController {
    private TokenService tokenService;


    @PostMapping("/register")
    public Mono<Void> registerToken(@Valid @RequestBody String token) {
        return tokenService.registerToken(token);
    }
}

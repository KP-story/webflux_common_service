package com.delight.notify.api.controller;

import com.delight.gaia.auth.annotation.RequiredLogin;
import com.delight.notify.api.model.UserNotification;
import com.delight.notify.api.service.UserNotifyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/notify/user")
@RestController
@AllArgsConstructor
public class UserNotifyController {
    private UserNotifyService userNotifyService;

    @GetMapping("/forward")
    @RequiredLogin
    Flux<UserNotification> loadForwardUserNotification(@RequestParam(required = false) String type, @RequestParam(required = false) String category, @RequestParam(required = false) Long firstNotifyId, @RequestParam(defaultValue = "20") int pageSize) {
        return userNotifyService.loadForwardUserNotification(type, category, firstNotifyId, pageSize);
    }


    @GetMapping()
    @RequiredLogin
    Flux<UserNotification> loadBackwardUserNotification(@RequestParam(required = false) String type, @RequestParam(required = false) String category, @RequestParam(required = false) Long lastNotifyId, @RequestParam(defaultValue = "20") int pageSize) {
        return userNotifyService.loadBackwardUserNotification(type, category, lastNotifyId, pageSize);
    }


    @RequiredLogin
    @PutMapping("/markRead/{notifyId}")
    Mono<Void> markRead(@PathVariable Long notifyId) {
        return userNotifyService.markRead(notifyId);

    }
    @RequiredLogin
    @PutMapping("/reset-counter")
    Mono<Void> resetCounter() {
        return userNotifyService.resetCounter();
    }
    @RequiredLogin
    @PutMapping("/markRead/all")
    Mono<Void> markReadAll() {
        return userNotifyService.markReadAll();
    }

    @RequiredLogin
    @GetMapping("/count-unread")
    Mono<Integer> countNotify() {
        return userNotifyService.countNotify();
    }

}

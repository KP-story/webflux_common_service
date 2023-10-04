package com.delight.notify.api.controller;

import com.delight.gaia.auth.annotation.RequiredPermission;
import com.delight.notify.api.model.request.AppNotificationRQ;
import com.delight.notify.api.model.request.UserNotificationRQ;
import com.delight.notify.service.impl.NotifyServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RequestMapping("/notify")
@RestController
@AllArgsConstructor
public class NotificationController {
    private NotifyServiceImpl notifyService;

    @PostMapping("/send-to-users")
    @RequiredPermission("notify:sendToUsers")
    public Mono<Long> sendToUsers(@Valid @RequestBody UserNotificationRQ userNotificationRQ) {
        return notifyService.sendToUsers(userNotificationRQ);
    }

    @PostMapping("/send-to-app")
    @RequiredPermission("notify:sendToApp")
    public Mono<Long> sendToApp(@Valid @RequestBody AppNotificationRQ appNotificationRQ) {
        return notifyService.sendToApp(appNotificationRQ);
    }
}

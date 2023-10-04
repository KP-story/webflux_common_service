package com.delight.notify.scheduler;

import com.delight.notify.constant.Timer;
import com.delight.notify.dao.repo.UserNotificationRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class NotificationVacuum {

    private UserNotificationRepo userNotificationRepo;

    @Scheduled(cron = Timer.WEEKLY_AT_SATURDAY_4AM)
    public void clearNotification() {
        userNotificationRepo.deleteBefore(LocalDateTime.now().minusDays(35)).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}

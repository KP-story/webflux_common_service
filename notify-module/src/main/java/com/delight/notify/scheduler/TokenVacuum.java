package com.delight.notify.scheduler;

import com.delight.notify.constant.Timer;
import com.delight.notify.dao.repo.UserTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class TokenVacuum {
    private UserTokenRepo userTokenRepo;

    @Scheduled(cron = Timer.WEEKLY_AT_SATURDAY_4AM)
    public void clearUserToken() {
        userTokenRepo.deleteBefore(LocalDateTime.now().minusDays(30)).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}

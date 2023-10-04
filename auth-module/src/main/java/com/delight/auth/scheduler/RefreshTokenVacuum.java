package com.delight.auth.scheduler;

import com.delight.auth.constant.Timer;
import com.delight.auth.dao.repo.OtpRepo;
import com.delight.auth.dao.repo.RefreshTokenRepo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class RefreshTokenVacuum {
    private RefreshTokenRepo refreshTokenRepo;
    private OtpRepo otpRepo;

    @Scheduled(cron = Timer.WEEKLY_AT_SATURDAY_4AM)
    public void clearRefreshToken() {
        LocalDateTime now = LocalDateTime.now();
        refreshTokenRepo.deleteBefore(now).subscribeOn(Schedulers.boundedElastic()).subscribe();
        otpRepo.deleteBefore(now).subscribeOn(Schedulers.boundedElastic()).subscribe();
    }
}

package com.delight.auth.dao.repo.jpa;

import com.delight.auth.dao.entity.RefreshTokenEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface RefreshTokenJpaRepo extends JPABaseRepo<RefreshTokenEntity, Long> {
    @Query("delete from refresh_token where  id not in ( select t.id from refresh_token t where t.user_id = $2 and app = $4 and device_id!=$1 order by t.updated_time desc limit $3) and id <> $5 and  user_id = $2 and app = $4 returning device_id")
    Flux<String> deleteExceedSession(String deviceId, Long userId, Short maxSession, String app, Long ignoreId);

    Mono<Void> deleteByUserIdAndApp(Long userId, String app);

    Mono<Void> deleteAllByUserIdAndDeviceIdAndApp(Long userId, String deviceId, String app);

    Mono<RefreshTokenEntity> findByTokenAndExpiredTimeAfterAndApp(String token, LocalDateTime now, String app);

    Mono<Void> deleteByExpiredTimeBefore(LocalDateTime now);

}

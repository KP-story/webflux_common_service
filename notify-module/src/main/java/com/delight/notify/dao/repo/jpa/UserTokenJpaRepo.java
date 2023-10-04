package com.delight.notify.dao.repo.jpa;

import com.delight.gaia.jpa.repo.JPABaseRepo;
import com.delight.notify.dao.entity.UserTokenEntity;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface UserTokenJpaRepo extends JPABaseRepo<UserTokenEntity, Long> {
    Mono<UserTokenEntity> findByTokenAndAppCode(String token, String appCode);

    Flux<UserTokenEntity> findAllByDeviceIdInAndUserIdAndAppCode(List<String> deviceId, Long userId, String appCode);

    @Query("delete from user_token where  id not in ( select t.id from user_token t where  app_code = $2 and user_id=$5 and token!=$1 and device_id!=$4 order by t.id desc limit $3) and   app_code = $2 and user_id=$5 or device_id=$4 returning token")
    Flux<String> deleteByTokenAndAppCode(String token, String appCode, Short maxSession, String deviceId, Long userId);

    Mono<Void> deleteByIdInAndAppCode(List<Long> tokenIds, String appCode);

    Mono<Void> deleteByTokenInAndAppCode(List<String> tokens, String appCode);

    Flux<UserTokenEntity> findAllByUserIdInAndAppCode(List<Long> userIds, String appCode);

    Flux<UserTokenEntity> findAllByUserIdAndAppCode(Long userId, String appCode);

    Flux<UserTokenEntity> findAllByUserIdInAndAppCodeAndDeviceIdNotIn(List<Long> userIds, String appCode, List<String> ignoreDeviceIds);

    Mono<Void> deleteByCreatedTimeBefore(LocalDateTime now);

    Mono<Void> deleteByTokenAndAppCode(String token, String appCode);


}

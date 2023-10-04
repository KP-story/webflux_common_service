package com.delight.auth.dao.repo.jpa;

import com.delight.auth.dao.entity.UserEntity;
import com.delight.gaia.jpa.repo.JPABaseRepo;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

public interface UserJpaRepo extends JPABaseRepo<UserEntity, Long> {
    Mono<UserEntity> findAllByStatusAndPhoneOrEmail(Byte status, String phone, String email);

    Mono<UserEntity> findByEmail(String email);

    Mono<UserEntity> findByPhone(String phone);

    @Query("update account set has_avatar =$2 where id =$1")
    @Modifying
    Mono<Void> updateUserAvatar(Long userId, Boolean hasAvatar);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByPhone(String phone);


    @Query("update account set lang =$2 where id =$1")
    @Modifying
    Mono<Void> updateUserLang(Long userId, String lang);


}

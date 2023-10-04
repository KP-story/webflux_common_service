package com.delight.auth.dao.repo;

import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.gaia.jpa.repo.BaseRepo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface UserRepo extends BaseRepo<UserEntity, Long> {
    Mono<UserEntity> findByPhoneOrEmail(String phone, String email);

    Mono<UserEntity> findByEmail(String email);

    Flux<UserViewInfo> listUserViewInfo(List<Long> ids);

    Flux<UserViewInfo> listUserViewInfo(Set<Long> ids);

    Flux<UserPublicInfo> searchUser(String keyword, int page, int pageSize);

    Mono<Void> updateAvatar(Long userId, Boolean hasAvatar);

    Mono<Void> updateLang(Long userId, String lang);

    Mono<UserEntity> findByPhone(String phone);

    Mono<Boolean> existsByEmail(String email);

    Mono<Boolean> existsByPhone(String phone);

    Mono<UserViewInfo> findUserViewInfo(Long id);


}

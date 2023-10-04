package com.delight.auth.dao.repo.impl;

import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.auth.dao.repo.UserRepo;
import com.delight.auth.dao.repo.jpa.UserJpaRepo;
import com.delight.auth.dao.repo.jpa.UserQueryRepo;
import com.delight.gaia.base.constant.Status;
import com.delight.gaia.jpa.repo.AbsBaseRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
public class UserRepoImpl extends AbsBaseRepo<UserEntity, Long, UserJpaRepo> implements UserRepo {
    private UserQueryRepo userQueryRepo;

    @Override
    public Mono<UserEntity> findByPhoneOrEmail(String phone, String email) {
        return jpaRepo.findAllByStatusAndPhoneOrEmail(Status.ACTIVE, phone, email);
    }

    @Override
    public Mono<UserEntity> findByEmail(String email) {
        return jpaRepo.findByEmail(email);
    }

    @Override
    public Flux<UserViewInfo> listUserViewInfo(List<Long> ids) {
        return userQueryRepo.listUserViewInfo(ids);
    }

    @Override
    public Flux<UserViewInfo> listUserViewInfo(Set<Long> ids) {
        return userQueryRepo.listUserViewInfo(ids);
    }

    @Override
    public Flux<UserPublicInfo> searchUser(String keyword, int page, int pageSize) {
        return userQueryRepo.searchUser(keyword, page, pageSize);
    }

    @Override
    public Mono<Void> updateAvatar(Long userId, Boolean hasAvatar) {
        return jpaRepo.updateUserAvatar(userId, hasAvatar);
    }

    @Override
    public Mono<Void> updateLang(Long userId, String lang) {
        return jpaRepo.updateUserLang(userId, lang);
    }

    @Override
    public Mono<UserEntity> findByPhone(String phone) {
        return jpaRepo.findByPhone(phone);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return jpaRepo.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existsByPhone(String phone) {
        return jpaRepo.existsByPhone(phone);
    }

    @Override
    public Mono<UserViewInfo> findUserViewInfo(Long id) {
        return userQueryRepo.listUserViewInfo(id);
    }

}

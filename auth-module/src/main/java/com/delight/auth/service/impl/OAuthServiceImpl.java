package com.delight.auth.service.impl;

import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.service.AuthService;
import com.delight.auth.api.service.OAuthService;
import com.delight.auth.constant.OAuthType;
import com.delight.auth.constant.ProviderType;
import com.delight.auth.dao.entity.LinkedAccountEntity;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.auth.dao.repo.LinkedAccountRepo;
import com.delight.auth.dao.repo.UserRepo;
import com.delight.auth.mapper.SubjectInfoMapper;
import com.delight.auth.service.impl.oauth.OAuthIdentity;
import com.delight.auth.service.impl.oauth.OAuthProvider;
import com.delight.gaia.base.constant.Status;
import com.delight.gaia.base.exception.CommandFailureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.delight.auth.constant.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthServiceImpl implements OAuthService {
    private final List<OAuthProvider> listOAuthProviders;
    private final AuthService authService;
    private final LinkedAccountRepo linkedAccountRepo;
    private final UserRepo userRepo;
    private final SubjectInfoMapper subjectInfoMapper;
    private Map<OAuthType, OAuthProvider> oAuthProviders;

    @PostConstruct
    public void init() {
        oAuthProviders = new HashMap<>();
        for (var p : listOAuthProviders) {
            oAuthProviders.put(p.getType(), p);
        }
    }

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public Mono<LoginRS> loginUserOAuth(OAuthRQ oAuthRQ, String deviceId, String platform, String deviceName, String deviceOs, String appName, String appVersion, Double latitude, Double longitude) {

        OAuthProvider provider = oAuthProviders.get(oAuthRQ.getProvider());
        if (provider == null) {
            Mono.error(new CommandFailureException(OAUTH_PROVIDER_NOT_FOUND));
        }
        return provider.verifyOAuth(oAuthRQ).onErrorMap(throwable -> {
            log.error("login failed {} error ", oAuthRQ, throwable);
            return new CommandFailureException(LOGIN_FAIL);
        }).flatMap(oAuthIdentity -> {
            if (oAuthIdentity.getEmail() == null) {
                return Mono.error(new CommandFailureException(OAUTH_EMAIL_NOT_FOUND));
            } else {
                oAuthIdentity.setOAuthType(oAuthRQ.getProvider());
            }
            return _doLoginOrSignUpAction(oAuthIdentity);

        }).map(subjectInfoMapper::entityToSb).flatMap(subjectInfo -> authService.handleLogin(subjectInfo, deviceId, platform, deviceName, deviceOs, appName, appVersion, latitude, longitude));
    }


    private Mono<UserEntity> _doLoginOrSignUpAction(OAuthIdentity oAuthIdentity) {
        Mono<LinkedAccountEntity> identity = linkedAccountRepo.findByTypeAndProviderId(ProviderType.valueOf(oAuthIdentity.getOAuthType().name()), oAuthIdentity.getId());
        return identity.flatMap(linkedAccountEntity -> userRepo.findById(linkedAccountEntity.getUserId()))
                .switchIfEmpty(Mono.defer(() -> userRepo.findByEmail(oAuthIdentity.getEmail()).map(userEntity -> {
                    if (!userEntity.getStatus().equals(Status.ACTIVE)) {
                        throw new CommandFailureException(ACCOUNT_INACTIVE);
                    }
                    return userEntity;

                }).switchIfEmpty(Mono.defer(() ->
                        createUser(oAuthIdentity)).flatMap(userEntity -> createLinkedAccount(oAuthIdentity, userEntity.getId())
                        .map(linkedAccountEntity -> userEntity)))));


    }

    public Mono<LinkedAccountEntity> createLinkedAccount(OAuthIdentity oAuthIdentity, Long userId) {
        LinkedAccountEntity linkedAccountDTO = new LinkedAccountEntity();
        linkedAccountDTO.setUserId(userId);
        linkedAccountDTO.setProviderType(ProviderType.valueOf(oAuthIdentity.getOAuthType().name()));
        linkedAccountDTO.setProviderId(oAuthIdentity.getId());
        return linkedAccountRepo.save(linkedAccountDTO);
    }

    public Mono<UserEntity> createUser(OAuthIdentity oAuthIdentity) {
        String email = oAuthIdentity.getEmail();
        String firstName = oAuthIdentity.getFirstName();
        String lastName = oAuthIdentity.getLastName();
        String profilePhoto = oAuthIdentity.getProfilePhoto();
        UserEntity userDTO = new UserEntity();
        userDTO.setEmail(email);
        userDTO.setStatus(Status.ACTIVE);
        userDTO.setFirstname(firstName);
        userDTO.setLastname(lastName);
//        userDTO.setAvatar(profilePhoto);
        //todo lam tiep
        userDTO.setCreatedTime(LocalDateTime.now());
        userDTO.setUpdatedTime(LocalDateTime.now());
        return userRepo.save(userDTO);

    }
}

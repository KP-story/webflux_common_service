package com.delight.auth.service.impl;

import com.delight.auth.api.model.UserSimpleInfo;
import com.delight.auth.api.model.request.LoginRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.model.response.TokenRS;
import com.delight.auth.api.service.AuthService;
import com.delight.auth.config.AuthConfig;
import com.delight.auth.constant.ProviderType;
import com.delight.auth.dao.entity.RefreshTokenEntity;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.auth.dao.repo.LinkedAccountRepo;
import com.delight.auth.dao.repo.RefreshTokenRepo;
import com.delight.auth.dao.repo.UserRepo;
import com.delight.auth.mapper.SubjectInfoMapper;
import com.delight.auth.utility.JwtTokenProvider;
import com.delight.auth.utility.PasswordUtil;
import com.delight.gaia.auth.context.SecurityUtils;
import com.delight.gaia.base.constant.MessageCode;
import com.delight.gaia.base.exception.CommandFailureException;
import com.delight.gaia.base.utility.TokenUtils;
import com.delight.notify.api.service.TokenService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;

import static com.delight.auth.constant.ErrorCode.LOGIN_FAIL;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final LinkedAccountRepo linkedAccountRepo;
    private final SubjectInfoMapper subjectInfoMapper;
    private final AuthConfig authConfig;
    private final TokenService tokenService;
    @Value("${auth.maxSession}")
    private Short maxSession;

    private static Date calculateExpirationDate(Date createdDate, Integer hourDuration) {
        if (hourDuration == null) {
            return null;
        }
        return new Date(createdDate.getTime() + hourDuration * 60 * 60 * 1000);
    }

    @PostConstruct
    void init() {
        maxSession--;
    }

    public Mono<UserSimpleInfo> checkLogin(LoginRQ loginRQ) {
        Mono<UserEntity> user = userRepo.findByPhoneOrEmail(loginRQ.getAccount(), loginRQ.getAccount());
        return user.switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(LOGIN_FAIL)))).flatMap(userEntity -> {
            String password = PasswordUtil.encodePassword(loginRQ.getPassword());
            return linkedAccountRepo.findByUserIdAndTypeAndProviderId(userEntity.getId(), ProviderType.PASSWORD, password).switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(LOGIN_FAIL)))).map(linkedAccountEntity -> subjectInfoMapper.entityToSb(userEntity));
        });
    }

    public Mono<LoginRS> handleLogin(UserSimpleInfo user, String deviceId, String platform, String deviceName, String deviceOs, String appName, String appVersion, Double latitude, Double longitude) {
        String refreshToken = TokenUtils.generateUUIdToken();
        LoginRS loginV2RS = new LoginRS();
        long id = user.getId();
        Date date = new Date();
        Date expired = calculateExpirationDate(date, authConfig.getIdTokenExpiration());
        loginV2RS.setIdToken(genIdToken(user, appName, date, expired));
        loginV2RS.setRefreshToken(refreshToken);
        RefreshTokenEntity refreshTokenDTO = new RefreshTokenEntity();
        LocalDateTime now = LocalDateTime.now();
        refreshTokenDTO.setDeviceId(deviceId);
        refreshTokenDTO.setPlatform(platform);
        refreshTokenDTO.setDeviceOs(deviceOs);
        refreshTokenDTO.setDeviceName(deviceName);
        refreshTokenDTO.setUserId(id);
        refreshTokenDTO.setToken(refreshToken);
        refreshTokenDTO.setDeviceId(deviceId);
        refreshTokenDTO.setApp(appName);
        refreshTokenDTO.setAppVersion(appVersion);
        refreshTokenDTO.setLatitude(latitude);
        refreshTokenDTO.setLongitude(longitude);
        refreshTokenDTO.setCreatedTime(now);
        refreshTokenDTO.setExpiredTime(now.plusHours(authConfig.getRefreshTokenExpiration()));
        loginV2RS.setSimpleInfo(user);
        loginV2RS.setExpiredTime(refreshTokenDTO.getExpiredTime());

        return refreshTokenRepo.save(refreshTokenDTO).map(refreshTokenEntity -> {
            refreshTokenRepo.deleteByDeviceIdAndUserIdIgnoreIdOrExceedSession(deviceId, id, refreshTokenEntity.getId(), maxSession, appName).filter(s -> !s.equals(deviceId)).collectList().doOnSuccess(deviceIds -> {
                tokenService.unregisterDeviceIds(deviceIds, user.getId(), appName).subscribe();
            }).subscribe();
            return loginV2RS;
        });
    }

    @SneakyThrows
    private String genIdToken(UserSimpleInfo subjectInfo, String audience, Date now, Date expiredTime) {
        return JwtTokenProvider.generateHMACToken("all", authConfig.getIssuer(), subjectInfo, audience, authConfig.getPrivateKeyBytes(), authConfig.getKId(), now, expiredTime);
    }

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public Mono<LoginRS> login(LoginRQ loginRQ, String deviceId, String platform, String deviceName, String deviceOs, String app, String appVersion, Double latitude, Double longitude) {
        return checkLogin(loginRQ).flatMap(subjectInfo -> handleLogin(subjectInfo, deviceId, platform, deviceName, deviceOs, app, appVersion, latitude, longitude));
    }

    @Override
    public Mono<Void> logout(String deviceId) {
        if (deviceId == null) {
            return SecurityUtils.getRequester().flatMap(subject -> refreshTokenRepo.deleteByUserId(subject.getId(), subject.getClientInfo().getApp()));
        } else {
            return SecurityUtils.getRequester().flatMap(subject -> refreshTokenRepo.deleteByDeviceIdAndUserId(deviceId, subject.getId(), subject.getClientInfo().getApp()));
        }
    }


    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public Mono<TokenRS> refreshToken(String deviceId, String refreshToken, String app) {
        return refreshTokenRepo.findActiveByToken(refreshToken, app).switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(MessageCode.UNAUTHORIZED))))
                .flatMap(refreshTokenEntity -> {
                    String newToken = TokenUtils.generateUUIdToken();
                    refreshTokenEntity.setToken(newToken);
                    refreshTokenEntity.setDeviceId(deviceId);
                    refreshTokenEntity.setExpiredTime(LocalDateTime.now().plusHours(authConfig.getRefreshTokenExpiration()));
                    return userRepo.findById(refreshTokenEntity.getUserId()).map(subjectInfoMapper::entityToSb)
                            .flatMap(subjectInfo -> {
                                Date date = new Date();
                                Date expired = calculateExpirationDate(date, authConfig.getIdTokenExpiration());
                                String token = genIdToken(subjectInfo, refreshTokenEntity.getApp() == null ? deviceId : refreshTokenEntity.getApp(), date, expired);
                                TokenRS tokenRS = new TokenRS().setIdToken(token).setRefreshToken(newToken);
                                tokenRS.setExpiredTime(refreshTokenEntity.getExpiredTime());
                                return refreshTokenRepo.save(refreshTokenEntity).map(refreshTokenEntity1 -> tokenRS);
                            });

                });
    }
}

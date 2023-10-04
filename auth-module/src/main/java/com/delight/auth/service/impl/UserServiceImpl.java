package com.delight.auth.service.impl;

import com.delight.assets.api.service.FileService;
import com.delight.auth.api.model.User;
import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.api.model.request.CreateUserRQ;
import com.delight.auth.api.model.request.UpdateUserRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.model.response.OtpRS;
import com.delight.auth.api.model.response.TokenRS;
import com.delight.auth.api.model.response.VerifyOtpRS;
import com.delight.auth.api.service.AuthService;
import com.delight.auth.api.service.UserService;
import com.delight.auth.cache.UserDisplayCache;
import com.delight.auth.constant.*;
import com.delight.auth.dao.entity.LinkedAccountEntity;
import com.delight.auth.dao.entity.OtpConfigEntity;
import com.delight.auth.dao.entity.OtpEntity;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.auth.dao.repo.LinkedAccountRepo;
import com.delight.auth.dao.repo.OtpConfigRepo;
import com.delight.auth.dao.repo.OtpRepo;
import com.delight.auth.dao.repo.UserRepo;
import com.delight.auth.mapper.SubjectInfoMapper;
import com.delight.auth.mapper.UserMapper;
import com.delight.auth.mapper.UserPublicInfoMapper;
import com.delight.auth.utility.PasswordUtil;
import com.delight.gaia.auth.context.SecurityUtils;
import com.delight.gaia.base.constant.MessageCode;
import com.delight.gaia.base.constant.Status;
import com.delight.gaia.base.exception.CommandFailureException;
import com.delight.gaia.base.message.GaiaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;

import static com.delight.auth.constant.ErrorCode.PHONE_EMAIL_EXISTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private static int VERIFY_CODE_TTL_MINUTES = 20;
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final UserPublicInfoMapper userPublicInfoMapper;
    private final LinkedAccountRepo linkedAccountRepo;
    private final AuthService authService;
    private final OtpRepo otpRepo;
    private final OtpConfigRepo otpConfigRepo;
    private final SubjectInfoMapper subjectInfoMapper;
    private final EmailSenderService emailSenderService;
    private final FileService fileService;
    private final UserDisplayCache userDisplayCache;

    @Value("${account.avatarAsset}")
    private String avatarConfig;

    public String generateOTPCode(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public String generateVerifyCode() {
        return RandomStringUtils.randomAlphanumeric(20);
    }

    @Override
    public Flux<UserViewInfo> listUserViewInfo(Set<Long> ids) {
        return userDisplayCache.getMultiple(ids).map(longUserViewInfoEntry -> longUserViewInfoEntry.getValue());
    }

    @Override
    public Flux<UserPublicInfo> searchUser(String keyword, int page, int pageSize) {
        return userRepo.searchUser(keyword, page, pageSize);
    }

    @Override
    public Mono<UserPublicInfo> getUserInfo(Long userId) {
        return userRepo.findById(userId).map(userPublicInfoMapper::entityToPublicInfo);
    }

    @Override
    public Mono<User> getDetailInfo() {
        return SecurityUtils.getRequester().flatMap(subject -> userRepo.findById(subject.getId())).map(userMapper::entityToDto).flatMap(user -> linkedAccountRepo.existByUserIdAndProviderType(user.getId(), ProviderType.PASSWORD).map(hasPassword -> {
            user.setHasPassword(hasPassword);
            return user;
        }));
    }


    @Override
    public Mono<Void> updateUserInfo(UpdateUserRQ updateUserRQ) {
        return SecurityUtils.getRequester()
                .flatMap(subject -> userRepo.findById(subject.getId()).flatMap(userEntity -> {
                    Mono check = null;
                    boolean updateName = false;
                    if (updateUserRQ.getEmail() != null) {
                        String email = updateUserRQ.getEmail().orElse(null);
                        if (email == null || !EmailValidator.getInstance().isValid(email)) {
                            return Mono.error(new CommandFailureException(MessageCode.BAD_REQUEST, "Email invalid"));
                        }
                        userEntity.setEmail(email);
                        check = userRepo.existsByEmail(email).flatMap(exists ->
                                exists ? Mono.error(new CommandFailureException(PHONE_EMAIL_EXISTED, "Email exists")) : Mono.just(true));
                    }
                    if (updateUserRQ.getPhone() != null) {
                        String phone = updateUserRQ.getPhone().orElse(null);
                        if (phone == null || phone.length() < 9 || phone.length() > 20) {
                            return Mono.error(new CommandFailureException(MessageCode.BAD_REQUEST, "Phone invalid"));
                        }
                        userEntity.setPhone(phone);

                        if (check == null)
                            check = userRepo.existsByPhone(phone).flatMap(exists -> exists ?
                                    Mono.error(new CommandFailureException(PHONE_EMAIL_EXISTED, "Phone exists")) : Mono.just(true));
                        else
                            check = Mono.zip(check, userRepo.existsByPhone(phone).flatMap(exists -> exists ?
                                    Mono.error(new CommandFailureException(PHONE_EMAIL_EXISTED, "Phone exists")) : Mono.just(true)));
                    }
                    if (updateUserRQ.getFirstname() != null) {
                        String firstName = updateUserRQ.getFirstname().orElse(null);
                        if (firstName == null || firstName.length() < 1) {
                            return Mono.error(new CommandFailureException(MessageCode.BAD_REQUEST, "FirstName invalid"));

                        }
                        userEntity.setFirstname(firstName);
                        updateName = true;
                    }
                    if (updateUserRQ.getLastname() != null) {
                        String lastname = updateUserRQ.getLastname().orElse(null);
                        if (lastname == null || lastname.length() < 1) {
                            return Mono.error(new CommandFailureException(MessageCode.BAD_REQUEST, "LastName invalid"));

                        }
                        userEntity.setLastname(lastname);
                        updateName = true;

                    }
                    if (updateUserRQ.getBirthday() != null) {
                        var birthday = updateUserRQ.getBirthday().orElse(null);
                        userEntity.setBirthday(birthday);
                    }
                    if (updateUserRQ.getGender() != null) {
                        var gender = updateUserRQ.getGender().orElse(null);
                        userEntity.setGender(gender);
                    }
                    if (check != null) {
                        return check.flatMap(b -> userRepo.save(userEntity)).then();

                    } else {
                        boolean finalUpdateName = updateName;
                        return userRepo.save(userEntity).doOnSuccess(userEntity1 -> {
                            if (finalUpdateName) {
                                GaiaMessage event = new GaiaMessage();
                                event.setBody(subject.getId());
                                event.setCommand(AccountEventType.UPDATE_NAME);
                            }

                        }).then();
                    }
                }));
    }

    @Override
    public Mono<Void> updateAvatar(FilePart filePart, Long contentLength) {
        return SecurityUtils.getRequester().flatMap(subject ->
                {
                    Mono<Void> update;
                    if (filePart == null) {
                        update = userRepo.updateAvatar(subject.getId(), null);
                    } else {
                        update = fileService.uploadFile(avatarConfig, subject.getId().toString(), contentLength, filePart).flatMap(uploadRS -> userRepo.updateAvatar(subject.getId(), true));
                    }
                    update.doOnSuccess(unused -> {
                        GaiaMessage event = new GaiaMessage();
                        event.setBody(subject.getId());
                        event.setCommand(AccountEventType.UPDATE_AVATAR);

                    });
                    return update;
                }
        ).then();


    }

    @Override
    public Mono<Void> updateLanguage(String lang) {
        return SecurityUtils.getRequester().flatMap(subject ->
                userRepo.updateLang(subject.getId(), lang)
        );
    }

    @Override
    public Mono<TokenRS> updatePassword(String oldPassword, String newPassword, boolean keepLogin) {
        return SecurityUtils.getRequester().flatMap(subject -> linkedAccountRepo.findByUserIdAndProviderType(subject.getId(), ProviderType.PASSWORD)
                .switchIfEmpty(Mono.defer(() -> {
                    LinkedAccountEntity linkedAccountEntity = new LinkedAccountEntity();
                    linkedAccountEntity.setStatus(Status.ACTIVE);
                    linkedAccountEntity.setUserId(subject.getId());
                    linkedAccountEntity.setProviderType(ProviderType.PASSWORD);
                    linkedAccountEntity.setProviderId(PasswordUtil.encodePassword(newPassword));
                    return Mono.just(linkedAccountEntity);
                })).flatMap(linkedAccountEntity -> {
                    if (PasswordUtil.encodePassword(oldPassword).equals(linkedAccountEntity.getProviderId())) {
                        linkedAccountEntity.setProviderId(PasswordUtil.encodePassword(newPassword));
                        return linkedAccountRepo.save(linkedAccountEntity)
                                .flatMap(__ -> keepLogin ? Mono.just(true) : authService.logout(null).thenReturn(false)).flatMap(__ ->
                                        userRepo.findById(subject.getId()).flatMap(userEntity -> authService.handleLogin(subjectInfoMapper.entityToSb(userEntity), subject.getClientInfo().getDeviceId(), subject.getClientInfo().getPlatform().name(), null, null, subject.getClientInfo().getApp(), null, null, null)));
                    } else {
                        return Mono.error(new CommandFailureException(ErrorCode.OLD_PASSWORD_WRONG));
                    }
                }));
    }

    @Override
    public Mono<Void> delete() {
        return SecurityUtils.getRequester().flatMap(subject ->
                userRepo.findById(subject.getId())
        ).flatMap(userEntity -> {
            userEntity.setStatus(Status.DELETED);
            return userRepo.save(userEntity);
        }).flatMap(userEntity -> authService.logout(null));
    }

    @Override
    public Mono<Void> setPassword(String email, String phone, String app, String password, String code) {
        Mono<UserEntity> userEntityMono;
        String account;
        if (email != null) {
            userEntityMono = userRepo.findByEmail(email);
            account = email;
        } else {
            userEntityMono = userRepo.findByPhone(phone);
            account = phone;
        }
        return userEntityMono.switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(ErrorCode.ACCOUNT_NOT_FOUND)))).flatMap(userEntity ->
                otpRepo.findActiveOtp(account, OtpType.PW_RS.name(), app, LocalDateTime.now(), code)
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(ErrorCode.OTP_INVALID)))).flatMap(otpEntity -> {
                            otpRepo.deleteByAccountAndTypeAndApp(account, OtpType.PW_RS.name(), app).subscribe();
                            return linkedAccountRepo.findByUserIdAndProviderType(userEntity.getId(), ProviderType.PASSWORD)
                                    .switchIfEmpty(Mono.defer(() -> {
                                        LinkedAccountEntity linkedAccountEntity = new LinkedAccountEntity();
                                        linkedAccountEntity.setStatus(Status.ACTIVE);
                                        linkedAccountEntity.setUserId(userEntity.getId());
                                        linkedAccountEntity.setProviderType(ProviderType.PASSWORD);
                                        linkedAccountEntity.setProviderId(PasswordUtil.encodePassword(password));
                                        return Mono.just(linkedAccountEntity);
                                    })).flatMap(linkedAccountEntity -> {
                                        linkedAccountEntity.setProviderId(PasswordUtil.encodePassword(password));
                                        return linkedAccountRepo.save(linkedAccountEntity);
                                    });

                        }).then());


    }

    @Override
    public Mono<OtpRS> forgetPassword(String email, String phone, String app) {
        Mono<UserEntity> userEntityMono;
        boolean isEmail = false;
        String account;
        if (email != null) {
            userEntityMono = userRepo.findByEmail(email);
            isEmail = true;
            account = email;
        } else {
            account = phone;
            userEntityMono = userRepo.findByPhone(phone);
        }
        boolean finalIsEmail = isEmail;
        return userEntityMono.switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(ErrorCode.ACCOUNT_NOT_FOUND))))
                .flatMap(userEntity -> sendOtp(account, OtpType.PW_RS, app, (otp, otpConfigEntity) -> {
                    if (finalIsEmail) {
                        emailSenderService.sendEmailTemplate(otpConfigEntity, otp, userEntity);
                    }

                }));
    }

    @Override
    public Mono<VerifyOtpRS> verifyOtp(String account, String otp, String app, OtpType type) {
        LocalDateTime now = LocalDateTime.now();
        return otpRepo.findActiveOtp(account, type.name(), app, now, otp)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CommandFailureException(ErrorCode.OTP_INVALID)))).flatMap(otpEntity -> {
                    otpEntity.setOtp(generateVerifyCode());
                    otpEntity.setExpiredTime(now.plusMinutes(VERIFY_CODE_TTL_MINUTES));
                    return otpRepo.save(otpEntity).map(otpEntity1 -> new VerifyOtpRS().setVerifyCode(otpEntity1.getOtp()).setExpiredTime(otpEntity1.getExpiredTime()));
                });
    }

    public Mono<OtpRS> sendOtp(String account, OtpType otpType, String app, FunctionSendOtp functionSendOtp) {
        LocalDateTime now = LocalDateTime.now();
        return otpConfigRepo.findByApp(app, otpType).switchIfEmpty(Mono.error(new CommandFailureException(ErrorCode.APP_INVALID))).flatMap(otpConfigEntity -> {
            Mono<OtpEntity> activeOtp = otpRepo.findActiveOtp(account, otpType.name(), app, now);
            return activeOtp.flatMap(otpEntity -> {
                if (otpEntity.getCreatedTime().plusSeconds(otpConfigEntity.getResendTime()).isBefore(now)) {
                    return otpRepo.deleteByAccountAndTypeAndApp(account, otpType.name(), app).flatMap(__ -> Mono.empty());
                } else {
                    return Mono.just(otpEntity);
                }

            }).switchIfEmpty(Mono.defer(() -> {
                OtpEntity otpEntity = new OtpEntity();
                String otp = generateOTPCode(otpConfigEntity.getOtpLength());
                otpEntity.setOtp(otp);
                otpEntity.setAccount(account);
                otpEntity.setType(otpType.name());
                otpEntity.setApp(app);
                otpEntity.setExpiredTime(now.plusSeconds(otpConfigEntity.getTtl()));
                functionSendOtp.apply(otp, otpConfigEntity);
                return otpRepo.save(otpEntity);

            })).map(otpEntity -> new OtpRS().setAccount(account).setExpiredTime(otpEntity.getCreatedTime().plusSeconds(otpConfigEntity.getResendTime())));


        });

    }

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public Mono<LoginRS> createUser(CreateUserRQ rq, String deviceId, String platform, String deviceName, String deviceOs, String app, String appVersion, Double latitude, Double longitude) {
        Mono<UserEntity> userEntityMono;
        if (!EmailValidator.getInstance().isValid(rq.getEmail())) {
            throw new CommandFailureException(MessageCode.BAD_REQUEST, "Email invalid");
        }

        if (rq.getPhone() == null) {
            userEntityMono = userRepo.findByEmail(rq.getEmail());
        } else {
            userEntityMono = userRepo.findByPhoneOrEmail(rq.getPhone(), rq.getEmail());
        }
        return userEntityMono.flatMap(__ -> Mono.error(new CommandFailureException(PHONE_EMAIL_EXISTED))).switchIfEmpty(Mono.defer(() -> {
            UserEntity newUserEntity = new UserEntity();
            newUserEntity.setEmail(rq.getEmail());
            newUserEntity.setStatus(Status.ACTIVE);
            newUserEntity.setPhone(rq.getPhone());
            newUserEntity.setFirstname(rq.getFirstname());
            newUserEntity.setLastname(rq.getLastname());
            newUserEntity.setGender(rq.getGender());
            return userRepo.save(newUserEntity).flatMap(userEntity -> {
                LinkedAccountEntity newLinkedAccountEntity = new LinkedAccountEntity();
                newLinkedAccountEntity.setUserId(userEntity.getId());
                newLinkedAccountEntity.setProviderId(PasswordUtil.encodePassword(rq.getPassword()));
                newLinkedAccountEntity.setProviderType(ProviderType.PASSWORD);
                newLinkedAccountEntity.setStatus(Status.ACTIVE);
                return linkedAccountRepo.save(newLinkedAccountEntity).map(linkedAccountEntity -> userEntity);

            });
        })).flatMap(userEntity -> authService.handleLogin(subjectInfoMapper.entityToSb((UserEntity) userEntity), deviceId, platform, deviceName, deviceOs, app, appVersion, latitude, longitude));
    }

    public interface FunctionSendOtp {
        void apply(String otp, OtpConfigEntity otpConfigEntity);
    }
}



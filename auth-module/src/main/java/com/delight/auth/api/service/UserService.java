package com.delight.auth.api.service;

import com.delight.auth.api.model.User;
import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.api.model.request.CreateUserRQ;
import com.delight.auth.api.model.request.UpdateUserRQ;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.model.response.OtpRS;
import com.delight.auth.api.model.response.TokenRS;
import com.delight.auth.api.model.response.VerifyOtpRS;
import com.delight.auth.constant.OtpType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserService {
    Flux<UserViewInfo> listUserViewInfo(Set<Long> ids);

    Flux<UserPublicInfo> searchUser(String keyword, int page, int pageSize);

    Mono<UserPublicInfo> getUserInfo(Long userId);

    Mono<User> getDetailInfo();


    Mono<Void> updateUserInfo(UpdateUserRQ user);

    Mono<Void> updateAvatar(FilePart filePart, Long contentLength);

    Mono<Void> updateLanguage(String lang);

    Mono<TokenRS> updatePassword(String oldPassword, String newPassword, boolean keepLogin);

    Mono<Void> delete();

    Mono<OtpRS> forgetPassword(String email, String phone, String app);

    Mono<VerifyOtpRS> verifyOtp(String account, String otp, String app, OtpType type);


    Mono<LoginRS> createUser(CreateUserRQ rq, String deviceId, String platform, String deviceName, String deviceOs, String app, String appVersion, Double latitude, Double longitude);

    Mono<Void> setPassword(String email, String phone, String app, String password, String code);

}

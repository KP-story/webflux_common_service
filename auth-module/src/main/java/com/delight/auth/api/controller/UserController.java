package com.delight.auth.api.controller;

import com.delight.auth.api.model.User;
import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.api.model.request.*;
import com.delight.auth.api.model.response.LoginRS;
import com.delight.auth.api.model.response.OtpRS;
import com.delight.auth.api.model.response.TokenRS;
import com.delight.auth.api.model.response.VerifyOtpRS;
import com.delight.auth.api.service.UserService;
import com.delight.gaia.auth.annotation.RequiredLogin;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@RequestMapping("/user")
@RestController
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/get-view-info")
    public Flux<UserViewInfo> listUserViewInfo(@RequestParam Set<Long> ids) {
        return userService.listUserViewInfo(ids);
    }

    @GetMapping("/get-user-info/{userId}")
    public Mono<UserPublicInfo> listUserViewInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/search")
    public Flux<UserPublicInfo> searchUser(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int pageSize) {
        return userService.searchUser(keyword, page, pageSize);
    }


    @GetMapping("/detail")
    @RequiredLogin
    public Mono<User> getDetailInfo() {
        return userService.getDetailInfo();
    }

    @DeleteMapping("/delete")
    @RequiredLogin
    public Mono<Void> delete() {
        return userService.delete();
    }

    @PutMapping("/change-password")
    @RequiredLogin
    public Mono<TokenRS> changePassword(@RequestBody ChangePasswordRQ rq) {
        return userService.updatePassword(rq.getOldPassword(), rq.getNewPassword(), rq.getKeepLogin());
    }

    @PutMapping("/update-language")
    @RequiredLogin
    public Mono<Void> updateLanguage(@RequestBody String lang) {
        return userService.updateLanguage(lang);
    }

    @PostMapping("/forget-password")
    public Mono<OtpRS> forgetPassword(@RequestParam(required = false) String email, @RequestParam(required = false) String phone, @RequestHeader(value = "app", required = false) String appName) {
        return userService.forgetPassword(email, phone, appName);
    }

    @PostMapping("/verify-otp")
    public Mono<VerifyOtpRS> verifyOtp(@RequestHeader(value = "app", required = false) String appName, @Valid @RequestBody VerifyOtpRQ verifyOtpRQ) {
        return userService.verifyOtp(verifyOtpRQ.getAccount(), verifyOtpRQ.getOtp(), appName, verifyOtpRQ.getType());
    }

    @PostMapping("/set-password")
    public Mono<Void> setPassword(@RequestBody @Valid SetPasswordRQ setPasswordRQ, @RequestHeader(value = "app", required = false) String appName) {
        return userService.setPassword(setPasswordRQ.getEmail(), setPasswordRQ.getPhone(), appName, setPasswordRQ.getPassword(), setPasswordRQ.getVerificationCode());
    }

    @PutMapping("/update")
    @RequiredLogin
    public Mono<Void> updateUserInfo(@RequestBody @Valid UpdateUserRQ user) {
        return userService.updateUserInfo(user);
    }

    @PutMapping(value = "/update/avatar")
    @RequiredLogin
    public Mono<Void> updateAvatar(@RequestPart("avatar") FilePart filePart, @RequestHeader(name = HttpHeaders.CONTENT_LENGTH) Long contentLength) {
        return userService.updateAvatar(filePart, contentLength);
    }

    @PostMapping("/create")
    public Mono<LoginRS> createUser(@RequestBody @Valid CreateUserRQ rq, @RequestHeader("device-id") String deviceId, @RequestHeader(value = "device-name", required = false) String deviceName,
                                    @RequestHeader(value = "device-os", required = false) String deviceOs, @RequestHeader(value = "app", required = false) String appName,
                                    @RequestHeader(value = "app-version", required = false) String appVersion, @RequestHeader(value = "latitude", required = false) Double latitude,
                                    @RequestHeader(value = "longitude", required = false) Double longitude, @RequestHeader(value = "platform") String platform) {
        return userService.createUser(rq, deviceId, platform, deviceName, deviceOs, appName, appVersion, latitude, longitude);
    }
}

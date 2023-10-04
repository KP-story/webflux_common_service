//package com.delight.auth.api.internal.controller;
//
//import com.delight.auth.config.AuthConfig;
//import com.delight.auth.constant.ErrorCode;
//import com.delight.gaia.auth.api.dto.response.Jwk;
//import com.delight.gaia.base.exception.CommandFailureException;
//import com.delight.gaia.core.messaging.server.route.MessagingAttribute;
//import com.delight.gaia.core.messaging.server.route.MessagingCommand;
//import com.delight.gaia.core.messaging.server.route.MessagingController;
//import lombok.RequiredArgsConstructor;
//import reactor.core.publisher.Mono;
//
//@RequiredArgsConstructor
//@MessagingController
//public class KeysController {
//    private final AuthConfig authConfig;
//
//    @MessagingCommand("auth/kid")
//    public Mono<Jwk> getSigningKeys(@MessagingAttribute("kId") String kId) {
//        if (kId.equals(authConfig.getKId())) {
//            String key = authConfig.getPrivateKey();
//            Jwk.Builder j = Jwk.newBuilder();
//            j.setH(key);
//            j.setKid(kId);
//            return Mono.just(j.build());
//        } else return Mono.error(new CommandFailureException(ErrorCode.KID_NOT_FOUND));
//    }
//
//
//}

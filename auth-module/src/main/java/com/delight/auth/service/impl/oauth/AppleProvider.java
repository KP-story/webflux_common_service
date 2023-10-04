package com.delight.auth.service.impl.oauth;

import com.delight.auth.api.model.request.OAuthRQ;
import com.delight.auth.constant.ErrorCode;
import com.delight.auth.constant.OAuthType;
import com.delight.auth.external.AppleClient;
import com.delight.auth.external.dto.AppleAccountInfo;
import com.delight.gaia.auth.jwt.JwtTokenVerifier;
import com.delight.gaia.base.converter.SecurityConverter;
import com.delight.gaia.base.exception.CommandFailureException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppleProvider implements OAuthProvider, CommandLineRunner {
    private final JwtTokenVerifier jwtTokenVerifier;
    private final ObjectMapper objectMapper;
    private final AppleClient appleClient;
    private Map<String, PublicKey> publicKeys = new HashMap<>();

    @Override
    public Mono<OAuthIdentity> verifyOAuth(OAuthRQ oAuthRQ) {
        boolean isVerified = true;
        AppleAccountInfo info = null;
        try {
            SignedJWT signedJWT = jwtTokenVerifier.parse(oAuthRQ.getAccessToken());
            jwtTokenVerifier.verify(signedJWT, publicKeys.get(signedJWT.getHeader().getKeyID()));
            info = objectMapper.readValue(signedJWT.getPayload().toBytes(), AppleAccountInfo.class);
        } catch (Exception ex) {
            log.error("JWT verify error", ex);
            isVerified = false;
        }
        if (!isVerified) {
            return Mono.error(new CommandFailureException(ErrorCode.LOGIN_FAIL));
        }
        String email=info.getEmail();
        if(email==null)
        {
            email=info.getSub()+"@icloud.com";
        }
        String userId = info.getSub();
        OAuthIdentity oAuthIdentity = new OAuthIdentity().setId(userId).setEmail(email)
                .setFirstName(oAuthRQ.getFirstName()).setLastName(oAuthRQ.getLastName());
        oAuthIdentity.setEmail(oAuthIdentity.getEmail());
        return Mono.just(oAuthIdentity);
    }

    @Scheduled(initialDelay = 5 * 60 * 1000, fixedDelay = 5 * 60 * 1000)
    public void loadApplePublicKeys() {
        appleClient.getPublicKeysFromApple().doOnSuccess(jwks -> {
            Map<String, PublicKey> keys = new HashMap<>();
            var tmp = publicKeys;
            for (var key : jwks.getKeys()) {
                try {
                    keys.put(key.getKid(), SecurityConverter.decodeRsaPublicKey(key.getN(), key.getE()));
                } catch (Exception e) {
                    log.error("decode error", e);
                }
            }
            publicKeys = keys;
            tmp.clear();
        }).subscribe();
    }

    @Override
    public OAuthType getType() {
        return OAuthType.APPLE;
    }


    @Override
    public void run(String... args) throws Exception {
        loadApplePublicKeys();
    }
}

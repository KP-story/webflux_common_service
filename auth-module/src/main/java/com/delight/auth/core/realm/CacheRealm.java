package com.delight.auth.core.realm;

import com.delight.auth.cache.AllowOriginCache;
import com.delight.auth.cache.PermissionCache;
import com.delight.auth.cache.UserDisplayCache;
import com.delight.auth.config.AuthConfig;
import com.delight.auth.core.subject.AuthSubject;
import com.delight.gaia.auth.context.PermissionStatus;
import com.delight.gaia.auth.context.Realm;
import com.delight.gaia.auth.subject.ClientInfo;
import com.delight.gaia.auth.subject.Subject;
import com.delight.gaia.auth.subject.SubjectInfo;
import com.delight.gaia.base.constant.AccountType;
import com.delight.gaia.base.vo.UserDisplayInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA256;

@Component
@AllArgsConstructor
public class CacheRealm implements Realm {
    private static List<String> allowsHeaders;
    private AuthConfig authConfig;
    private PermissionCache permissionCache;
    private AllowOriginCache allowOriginCache;
    private UserDisplayCache userDisplayCache;

    {
        allowsHeaders =
                Arrays.asList(
                        "Authorization", "Content-Type", "Accept", "Origin", "User-Agent", "DNT",
                        "Cache-Control", "X-Mx-ReqToken", "Keep-Alive", "X-Requested-With", "If-Modified-Since",
                        "login_token", "timezone", "expiry", "uid", "client", "sessionid", "token-type",
                        "device-id",
                        "version", "Accept-Language"
                        , "user-agent", "timeStamp", "timestamp", "app", "platform", "device-name",
                        "device-os", "config", "app-version");
    }

    @Override
    public Flux<PermissionStatus> checkPermission(Long userId, String... permissions) {
        return null;
    }

    @Override
    public Flux<PermissionStatus> checkPermission(Long userId, AccountType accountType, String... permissions) {
        return null;
    }

    @Override
    public Mono<Key> getSigningKeys(String kId) {
        SecretKeySpec keySpec = new SecretKeySpec(authConfig.getPrivateKeyBytes(), HMAC_SHA256);
        return Mono.just(keySpec);
    }

    @Override
    public Mono<UserDisplayInfo> getDisplayInfo(Long userId) {
        Mono mono = userDisplayCache.get(userId);
        return mono;
    }

    @Override
    public Mono<Subject> makeSubject(SubjectInfo requesterInfo, ClientInfo clientInfo) {
        if (requesterInfo == null) {
            requesterInfo = new SubjectInfo().setAccountType(AccountType.ANONYMOUS).setId(-1L);
        }
        return Mono.just(new AuthSubject(requesterInfo, permissionCache, this).setClientInfo(clientInfo));
    }


    @Override
    public Mono<Boolean> allowDomain(String domain) {
        return allowOriginCache.get(domain);
    }

    @Override
    public List<String> allowHeaders(String domain) {
        return allowsHeaders;
    }

    @Override
    public Flux<Map.Entry<Long, UserDisplayInfo>> getDisplayInfos(Set<Long> userIds) {
        Flux flux = userDisplayCache.getMultiple(userIds);
        return flux;
    }
}

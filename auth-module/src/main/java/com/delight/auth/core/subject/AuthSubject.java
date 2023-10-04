package com.delight.auth.core.subject;

import com.delight.auth.cache.PermissionCache;
import com.delight.auth.cache.dao.UserKey;
import com.delight.gaia.auth.context.Realm;
import com.delight.gaia.auth.subject.ClientInfo;
import com.delight.gaia.auth.subject.Subject;
import com.delight.gaia.auth.subject.SubjectInfo;
import com.delight.gaia.base.constant.AccountType;
import com.delight.gaia.base.exception.AuthorizationException;
import com.delight.gaia.base.vo.UserDisplayInfo;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AuthSubject implements Subject {
    private Set<String> subjectPermissions = null;
    private Map<Object, Object> extraInfos = new HashMap<>(0);
    private SubjectInfo subjectInfo;
    private ClientInfo clientInfo;
    private PermissionCache permissionCache;
    private Realm realm;
    private UserDisplayInfo userDisplayInfo;

    public AuthSubject(SubjectInfo subjectInfo, PermissionCache permissionCache, Realm realm) {
        this.subjectInfo = subjectInfo;
        this.permissionCache = permissionCache;
        this.realm = realm;
    }

    Mono<Set<String>> getPermission() {
        if (subjectPermissions != null) {
            return Mono.just(subjectPermissions);
        } else {
            return permissionCache.get(new UserKey().setUserId(getId()).setAccountType(AccountType.NORMAL)).doOnSuccess(result -> subjectPermissions = result);
        }
    }


    @Override
    public Long getId() {
        return subjectInfo.getId();
    }

    @Override
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    @Override
    public SubjectInfo getInfo() {
        return subjectInfo;
    }

    @Override
    public Mono<UserDisplayInfo> getDisplayInfo() {
        if (userDisplayInfo != null) {
            return Mono.just(userDisplayInfo);
        } else {
            return realm.getDisplayInfo(getId()).doOnSuccess(userDisplayInfo1 -> userDisplayInfo = userDisplayInfo1);
        }
    }

    @Override
    public Map<Object, Object> getExtraInfo() {
        return extraInfos;
    }

    @Override
    public Subject setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        return this;
    }


    @Override
    public Mono<Boolean> isPermitted(String permission) {
        return getPermission().map(subPermission -> subPermission.contains(permission));
    }

    @Override
    public Mono<Boolean> checkPermission(String permission) {
        return isPermitted(permission).flatMap(isPermit -> {
            if (isPermit) {
                return Mono.just(true);
            } else {
                return Mono.error(new AuthorizationException("you do not permission: " + permission));
            }
        });
    }
}

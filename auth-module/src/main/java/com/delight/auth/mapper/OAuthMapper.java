package com.delight.auth.mapper;

import com.delight.auth.external.dto.FAccountInfo;
import com.delight.auth.external.dto.GAccountInfo;
import com.delight.auth.service.impl.oauth.OAuthIdentity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface OAuthMapper {
    @Mappings({
            @Mapping(source = "picture.url", target = "profilePhoto"),
    })
    OAuthIdentity getOAuthIdentityFromFbAccountInfo(FAccountInfo fAccountInfo);

    @Mappings({
            @Mapping(source = "picture", target = "profilePhoto"),
    })
    OAuthIdentity getOAuthIdentityFromGGAccountInfo(GAccountInfo gAccountInfo);

}

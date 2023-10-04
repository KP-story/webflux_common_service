package com.delight.auth.mapper;

import com.delight.auth.external.dto.FAccountInfo;
import com.delight.auth.external.dto.GAccountInfo;
import com.delight.auth.external.dto.Picture;
import com.delight.auth.service.impl.oauth.OAuthIdentity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:31:03+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class OAuthMapperImpl implements OAuthMapper {

    @Override
    public OAuthIdentity getOAuthIdentityFromFbAccountInfo(FAccountInfo fAccountInfo) {
        if ( fAccountInfo == null ) {
            return null;
        }

        OAuthIdentity oAuthIdentity = new OAuthIdentity();

        oAuthIdentity.setProfilePhoto( fAccountInfoPictureUrl( fAccountInfo ) );
        oAuthIdentity.setId( fAccountInfo.getId() );
        oAuthIdentity.setFirstName( fAccountInfo.getFirstName() );
        oAuthIdentity.setMiddleName( fAccountInfo.getMiddleName() );
        oAuthIdentity.setLastName( fAccountInfo.getLastName() );
        oAuthIdentity.setEmail( fAccountInfo.getEmail() );

        return oAuthIdentity;
    }

    @Override
    public OAuthIdentity getOAuthIdentityFromGGAccountInfo(GAccountInfo gAccountInfo) {
        if ( gAccountInfo == null ) {
            return null;
        }

        OAuthIdentity oAuthIdentity = new OAuthIdentity();

        oAuthIdentity.setProfilePhoto( gAccountInfo.getPicture() );
        oAuthIdentity.setId( gAccountInfo.getId() );
        oAuthIdentity.setFirstName( gAccountInfo.getFirstName() );
        oAuthIdentity.setLastName( gAccountInfo.getLastName() );
        oAuthIdentity.setEmail( gAccountInfo.getEmail() );

        return oAuthIdentity;
    }

    private String fAccountInfoPictureUrl(FAccountInfo fAccountInfo) {
        if ( fAccountInfo == null ) {
            return null;
        }
        Picture picture = fAccountInfo.getPicture();
        if ( picture == null ) {
            return null;
        }
        String url = picture.getUrl();
        if ( url == null ) {
            return null;
        }
        return url;
    }
}

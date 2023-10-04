package com.delight.auth.mapper;

import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.gaia.base.vo.UserDisplayInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:31:04+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class UserViewMapperImpl implements UserViewMapper {

    @Override
    public UserViewInfo entityToViewDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserViewInfo userViewInfo = new UserViewInfo();

        userViewInfo.setLastname( userEntity.getLastname() );
        userViewInfo.setFirstname( userEntity.getFirstname() );
        userViewInfo.setHasAvatar( userEntity.isHasAvatar() );
        userViewInfo.setId( userEntity.getId() );

        return userViewInfo;
    }

    @Override
    public UserDisplayInfo toDisplayInfo(UserViewInfo userViewInfo) {
        if ( userViewInfo == null ) {
            return null;
        }

        UserDisplayInfo userDisplayInfo = new UserDisplayInfo();

        userDisplayInfo.setLastname( userViewInfo.getLastname() );
        userDisplayInfo.setFirstname( userViewInfo.getFirstname() );
        userDisplayInfo.setHasAvatar( userViewInfo.getHasAvatar() );

        return userDisplayInfo;
    }
}

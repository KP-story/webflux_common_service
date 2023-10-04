package com.delight.auth.mapper;

import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.dao.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:31:03+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class UserPublicInfoMapperImpl implements UserPublicInfoMapper {

    @Override
    public UserPublicInfo entityToPublicInfo(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserPublicInfo userPublicInfo = new UserPublicInfo();

        userPublicInfo.setLastname( userEntity.getLastname() );
        userPublicInfo.setFirstname( userEntity.getFirstname() );
        userPublicInfo.setHasAvatar( userEntity.isHasAvatar() );
        userPublicInfo.setId( userEntity.getId() );
        userPublicInfo.setBirthday( userEntity.getBirthday() );
        userPublicInfo.setGender( userEntity.getGender() );

        return userPublicInfo;
    }
}

package com.delight.auth.mapper;

import com.delight.auth.api.model.UserSimpleInfo;
import com.delight.auth.dao.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:31:03+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class SubjectInfoMapperImpl implements SubjectInfoMapper {

    @Override
    public UserSimpleInfo entityToSb(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        UserSimpleInfo userSimpleInfo = new UserSimpleInfo();

        userSimpleInfo.setId( userEntity.getId() );
        userSimpleInfo.setFirstname( userEntity.getFirstname() );
        userSimpleInfo.setLastname( userEntity.getLastname() );
        userSimpleInfo.setPhone( userEntity.getPhone() );
        userSimpleInfo.setEmail( userEntity.getEmail() );
        userSimpleInfo.setBirthday( userEntity.getBirthday() );
        userSimpleInfo.setHasAvatar( userEntity.isHasAvatar() );
        userSimpleInfo.setGender( userEntity.getGender() );
        userSimpleInfo.setLang( userEntity.getLang() );

        return userSimpleInfo;
    }
}

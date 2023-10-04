package com.delight.auth.mapper;

import com.delight.auth.api.model.User;
import com.delight.auth.dao.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:31:03+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User entityToDto(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User user = new User();

        user.setFirstname( userEntity.getFirstname() );
        user.setLastname( userEntity.getLastname() );
        user.setPhone( userEntity.getPhone() );
        user.setEmail( userEntity.getEmail() );
        user.setBirthday( userEntity.getBirthday() );
        user.setHasAvatar( userEntity.isHasAvatar() );
        user.setGender( userEntity.getGender() );
        user.setId( userEntity.getId() );
        user.setCreatedTime( userEntity.getCreatedTime() );
        user.setUpdatedTime( userEntity.getUpdatedTime() );

        return user;
    }
}

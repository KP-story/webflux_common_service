package com.delight.auth.mapper;

import com.delight.auth.api.model.User;
import com.delight.auth.dao.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User entityToDto(UserEntity userEntity);
}

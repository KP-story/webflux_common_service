package com.delight.auth.mapper;

import com.delight.auth.api.model.UserPublicInfo;
import com.delight.auth.dao.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPublicInfoMapper {
    UserPublicInfo entityToPublicInfo(UserEntity userEntity);
}

package com.delight.auth.mapper;

import com.delight.auth.api.model.UserSimpleInfo;
import com.delight.auth.dao.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectInfoMapper {

    UserSimpleInfo entityToSb(UserEntity userEntity);
}

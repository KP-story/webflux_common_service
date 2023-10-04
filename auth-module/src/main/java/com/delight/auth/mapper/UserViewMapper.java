package com.delight.auth.mapper;

import com.delight.auth.api.model.UserViewInfo;
import com.delight.auth.dao.entity.UserEntity;
import com.delight.gaia.base.vo.UserDisplayInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserViewMapper {
    UserViewInfo entityToViewDto(UserEntity userEntity);

    UserDisplayInfo toDisplayInfo(UserViewInfo userViewInfo);
}

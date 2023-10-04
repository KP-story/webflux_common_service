package com.delight.notify.mapper;

import com.delight.notify.dao.entity.AppEntity;
import com.delight.notify.model.AppInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppInfoMapper {
    AppInfo entityToInfo(AppEntity appEntity);
}

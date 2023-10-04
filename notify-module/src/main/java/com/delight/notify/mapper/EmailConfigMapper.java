package com.delight.notify.mapper;

import com.delight.notify.dao.entity.EmailProviderEntity;
import com.delight.notify.model.EmailConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailConfigMapper {
    EmailConfig entityToInfo(EmailProviderEntity emailProviderEntity);
}

package com.delight.notify.mapper;

import com.delight.notify.dao.entity.EmailProviderEntity;
import com.delight.notify.dao.entity.ParameterEntity;
import com.delight.notify.model.EmailConfig;
import java.util.LinkedHashMap;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:30:58+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class EmailConfigMapperImpl implements EmailConfigMapper {

    @Override
    public EmailConfig entityToInfo(EmailProviderEntity emailProviderEntity) {
        if ( emailProviderEntity == null ) {
            return null;
        }

        EmailConfig emailConfig = new EmailConfig();

        emailConfig.setCode( emailProviderEntity.getCode() );
        emailConfig.setProviderType( emailProviderEntity.getProviderType() );
        emailConfig.setDefaultFrom( emailProviderEntity.getDefaultFrom() );
        emailConfig.setDefaultFromName( emailProviderEntity.getDefaultFromName() );
        ParameterEntity parameterEntity = emailProviderEntity.getParameters();
        if ( parameterEntity != null ) {
            emailConfig.setParameters( new LinkedHashMap<String, String>( parameterEntity ) );
        }

        return emailConfig;
    }
}

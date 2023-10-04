package com.delight.notify.mapper;

import com.delight.notify.dao.entity.AppEntity;
import com.delight.notify.model.AppInfo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:30:58+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class AppInfoMapperImpl implements AppInfoMapper {

    @Override
    public AppInfo entityToInfo(AppEntity appEntity) {
        if ( appEntity == null ) {
            return null;
        }

        AppInfo appInfo = new AppInfo();

        appInfo.setId( appEntity.getId() );
        appInfo.setCode( appEntity.getCode() );
        appInfo.setTopic( appEntity.getTopic() );
        appInfo.setLoginTopic( appEntity.getLoginTopic() );
        appInfo.setKey( appEntity.getKey() );

        return appInfo;
    }
}

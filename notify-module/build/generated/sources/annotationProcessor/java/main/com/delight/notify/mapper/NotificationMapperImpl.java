package com.delight.notify.mapper;

import com.delight.notify.api.model.NotificationPayload;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-25T11:30:58+0700",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.jar, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl extends NotificationMapper {

    @Override
    public NotificationEntity notifyToEntity(NotificationPayload notificationPayload) {
        if ( notificationPayload == null ) {
            return null;
        }

        NotificationEntity notificationEntity = new NotificationEntity();

        try {
            notificationEntity.setData( mapToString( notificationPayload.getData() ) );
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        notificationEntity.setId( notificationPayload.getId() );
        notificationEntity.setType( notificationPayload.getType() );
        notificationEntity.setAppId( notificationPayload.getAppId() );
        notificationEntity.setCategory( notificationPayload.getCategory() );
        notificationEntity.setScope( notificationPayload.getScope() );
        notificationEntity.setTitle( notificationPayload.getTitle() );
        notificationEntity.setBody( notificationPayload.getBody() );
        notificationEntity.setIcon( notificationPayload.getIcon() );
        notificationEntity.setTitleLocKey( notificationPayload.getTitleLocKey() );
        notificationEntity.setTitleLocArgs( stringListToStringArray( notificationPayload.getTitleLocArgs() ) );
        notificationEntity.setBodyLocKey( notificationPayload.getBodyLocKey() );
        notificationEntity.setBodyLocArgs( stringListToStringArray( notificationPayload.getBodyLocArgs() ) );

        return notificationEntity;
    }

    @Override
    public UserNotificationEntity userNotifyToEntity(NotificationPayload notificationPayload) {
        if ( notificationPayload == null ) {
            return null;
        }

        UserNotificationEntity userNotificationEntity = new UserNotificationEntity();

        try {
            userNotificationEntity.setData( mapToString( notificationPayload.getData() ) );
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        userNotificationEntity.setId( notificationPayload.getId() );
        userNotificationEntity.setType( notificationPayload.getType() );
        userNotificationEntity.setAppId( notificationPayload.getAppId() );
        userNotificationEntity.setCategory( notificationPayload.getCategory() );
        userNotificationEntity.setScope( notificationPayload.getScope() );
        userNotificationEntity.setTitle( notificationPayload.getTitle() );
        userNotificationEntity.setBody( notificationPayload.getBody() );
        userNotificationEntity.setIcon( notificationPayload.getIcon() );
        userNotificationEntity.setTitleLocKey( notificationPayload.getTitleLocKey() );
        userNotificationEntity.setTitleLocArgs( stringListToStringArray( notificationPayload.getTitleLocArgs() ) );
        userNotificationEntity.setBodyLocKey( notificationPayload.getBodyLocKey() );
        userNotificationEntity.setBodyLocArgs( stringListToStringArray( notificationPayload.getBodyLocArgs() ) );
        userNotificationEntity.setAppCode( notificationPayload.getAppCode() );
        userNotificationEntity.setCreatedTime( notificationPayload.getCreatedTime() );

        return userNotificationEntity;
    }

    protected String[] stringListToStringArray(List<String> list) {
        if ( list == null ) {
            return null;
        }

        String[] stringTmp = new String[list.size()];
        int i = 0;
        for ( String string : list ) {
            stringTmp[i] = string;
            i++;
        }

        return stringTmp;
    }
}

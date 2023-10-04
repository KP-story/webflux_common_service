package com.delight.notify.mapper;

import com.delight.notify.api.model.NotificationPayload;
import com.delight.notify.dao.entity.NotificationEntity;
import com.delight.notify.dao.entity.UserNotificationEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class NotificationMapper {
    @Autowired
    private ObjectMapper objectMapper;

    @Mapping(source = "data", target = "data", qualifiedByName = "mapToString")
    public abstract NotificationEntity notifyToEntity(NotificationPayload notificationPayload);

    @Mapping(source = "data", target = "data", qualifiedByName = "mapToString")
    public abstract UserNotificationEntity userNotifyToEntity(NotificationPayload notificationPayload);

    @Named("mapToString")
    public String mapToString(Map<String, String> data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}

package com.almonium.infra.notification.mapper;

import com.almonium.infra.notification.dto.response.NotificationDto;
import com.almonium.infra.notification.model.entity.Notification;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface NotificationMapper {

    NotificationDto toDto(Notification notification);

    List<NotificationDto> toDto(List<Notification> notifications);
}

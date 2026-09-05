package com.almonium.user.core.mapper;

import com.almonium.user.core.dto.response.NotificationPreferencesDto;
import com.almonium.user.core.dto.response.UserInfo;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {LearnerMapper.class, InterestMapper.class})
public interface UserMapper {
    @Mapping(source = "profile.avatarUrl", target = "avatarUrl")
    @Mapping(source = "profile.uiPreferences", target = "uiPreferences")
    @Mapping(source = "profile.hidden", target = "hidden")
    @Mapping(source = "profile", target = "notifications")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "isPremium", ignore = true)
    @Mapping(target = "isAdmin", ignore = true)
    UserInfo userToUserInfo(User user);

    default NotificationPreferencesDto toNotificationPreferences(Profile profile) {
        return new NotificationPreferencesDto(profile == null || profile.isSocialEmailNotifications());
    }
}

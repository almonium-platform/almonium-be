package com.linguarium.user.mapper;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.dto.request.RegisterRequest;
import com.linguarium.config.security.oauth2.userinfo.OAuth2UserInfo;
import com.linguarium.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(source = "learner.targetLangs", target = "targetLangs")
    @Mapping(source = "learner.fluentLangs", target = "fluentLangs")
    @Mapping(target = "uiLang", ignore = true)
    @Mapping(target = "profilePicLink", ignore = true)
    @Mapping(target = "background", ignore = true)
    @Mapping(target = "streak", ignore = true)
    @Mapping(target = "tags", ignore = true)
    UserInfo userToUserInfo(User user);

    @Mapping(source = "info.id", target = "providerUserId")
    @Mapping(source = "info.email", target = "email")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "registered", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "learner", ignore = true)
    @Mapping(target = "incomingFriendships", ignore = true)
    @Mapping(target = "outgoingFriendships", ignore = true)
    User providerUserInfoToUser(OAuth2UserInfo info);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registered", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "providerUserId", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "learner", ignore = true)
    @Mapping(target = "incomingFriendships", ignore = true)
    @Mapping(target = "outgoingFriendships", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    User registerRequestToUser(RegisterRequest request);
}

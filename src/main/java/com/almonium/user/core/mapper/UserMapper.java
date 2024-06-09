package com.almonium.user.core.mapper;

import com.almonium.auth.oauth2.model.OAuth2Principal;
import com.almonium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.user.core.model.entity.User;
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
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    OAuth2Principal providerUserInfoToPrincipal(OAuth2UserInfo info);
}

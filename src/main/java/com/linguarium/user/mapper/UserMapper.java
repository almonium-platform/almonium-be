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
    UserInfo userToUserInfo(User user);

    @Mapping(source = "info.id", target = "providerUserId")
    @Mapping(source = "info.email", target = "email")
    @Mapping(target = "id", ignore = true)
    User providerUserInfoToUser(OAuth2UserInfo info);

    User registerRequestToUser(RegisterRequest request);
}

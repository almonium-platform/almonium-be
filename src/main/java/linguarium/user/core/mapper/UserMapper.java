package linguarium.user.core.mapper;

import linguarium.auth.local.dto.request.RegisterRequest;
import linguarium.auth.oauth2.model.entity.Principal;
import linguarium.auth.oauth2.model.userinfo.OAuth2UserInfo;
import linguarium.user.core.dto.UserInfo;
import linguarium.user.core.model.entity.User;
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
    Principal providerUserInfoToPrincipal(OAuth2UserInfo info);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registered", ignore = true)
    @Mapping(target = "profile", ignore = true)
    @Mapping(target = "learner", ignore = true)
    @Mapping(target = "incomingFriendships", ignore = true)
    @Mapping(target = "outgoingFriendships", ignore = true)
    @Mapping(target = "providerAccounts", ignore = true)
    User registerRequestToUser(RegisterRequest request);
}

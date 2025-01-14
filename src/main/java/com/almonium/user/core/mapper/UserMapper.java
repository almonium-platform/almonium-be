package com.almonium.user.core.mapper;

import com.almonium.auth.oauth2.other.model.entity.OAuth2Principal;
import com.almonium.auth.oauth2.other.model.userinfo.OAuth2UserInfo;
import com.almonium.user.core.dto.UserInfo;
import com.almonium.user.core.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(uses = {LearnerMapper.class, InterestMapper.class})
public interface UserMapper {
    @Mapping(source = "profile.avatarUrl", target = "avatarUrl")
    @Mapping(target = "streak", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "isPremium", ignore = true)
    UserInfo userToUserInfo(User user);

    @Mapping(target = "providerUserId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OAuth2Principal providerUserInfoToPrincipal(OAuth2UserInfo info);

    @Mapping(target = "firstName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "lastName", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "providerUserId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatePrincipalFromUserInfo(@MappingTarget OAuth2Principal principal, OAuth2UserInfo userInfo);
}

package com.linguarium.user.mapper;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserToUserInfoMapper {
    @Mapping(source = "learner.targetLangs", target = "targetLangs")
    @Mapping(source = "learner.fluentLangs", target = "fluentLangs")
    UserInfo userToUserInfo(User user);

    default String idToString(Long id) {
        return id != null ? id.toString() : null;
    }
}
package com.linguarium.user.mapper;

import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.dto.request.LocalRegisterRequest;
import com.linguarium.auth.dto.request.ProviderRegisterRequest;
import com.linguarium.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    @Mapping(source = "learner.targetLangs", target = "targetLangs")
    @Mapping(source = "learner.fluentLangs", target = "fluentLangs")
    UserInfo userToUserInfo(User user);

    User providerRegisterRequestToUser(ProviderRegisterRequest request);

    User localRegisterRequestToUser(LocalRegisterRequest request);
}

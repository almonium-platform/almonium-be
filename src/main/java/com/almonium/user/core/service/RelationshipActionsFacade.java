package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.dto.request.FriendshipRequestDto;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipAction;
import com.almonium.user.relationship.service.RelationshipPerspectiveResolver;
import com.almonium.user.relationship.service.RelationshipService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RelationshipActionsFacade {
    RelationshipService relationshipService;
    RelationshipPerspectiveResolver relationshipPerspectiveResolver;
    ProfileInfoService profileInfoService;

    public BaseProfileInfo manageFriendship(User user, UUID relationshipId, RelationshipAction action) {
        Relationship updatedRel = relationshipService.manageFriendship(user, relationshipId, action);
        User otherUser =
                relationshipPerspectiveResolver.resolve(user, updatedRel).counterpart();

        return profileInfoService.getUserProfileInfo(user.getId(), otherUser.getId());
    }

    public BaseProfileInfo blockUser(User user, UUID targetUserId) {
        relationshipService.blockUser(user, targetUserId);

        return profileInfoService.getUserProfileInfo(user.getId(), targetUserId);
    }

    public BaseProfileInfo createFriendshipRequest(User user, FriendshipRequestDto dto) {
        Relationship relationship = relationshipService.createFriendshipRequest(user, dto);
        User otherUser = relationship.getRequestee();
        return profileInfoService.getUserProfileInfo(user.getId(), otherUser.getId());
    }
}

package com.almonium.user.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.dto.response.BaseProfileInfo;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipAction;
import com.almonium.user.relationship.service.RelationshipService;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RelationshipActionsFacade {
    RelationshipService relationshipService;
    ProfileInfoService profileInfoService;

    @Transactional
    public BaseProfileInfo manageFriendship(User user, UUID relationshipId, RelationshipAction action) {
        relationshipService.manageFriendship(user, relationshipId, action);

        Relationship updatedRel = relationshipService
                .findById(relationshipId)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));

        User otherUser = updatedRel.getRequester().equals(user) ? updatedRel.getRequestee() : updatedRel.getRequester();

        return profileInfoService.getUserProfileInfo(user.getId(), otherUser.getId());
    }

    @Transactional
    public BaseProfileInfo blockUser(User user, UUID targetUserId) {
        relationshipService.blockUser(user, targetUserId);

        return profileInfoService.getUserProfileInfo(user.getId(), targetUserId);
    }
}

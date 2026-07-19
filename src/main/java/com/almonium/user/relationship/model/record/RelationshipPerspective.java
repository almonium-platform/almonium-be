package com.almonium.user.relationship.model.record;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import java.util.Optional;
import java.util.UUID;

public record RelationshipPerspective(
        Optional<Relationship> relationship,
        User counterpart,
        RelativeRelationshipStatus status,
        Boolean acceptsRequests,
        boolean profileVisible,
        boolean canRequest,
        boolean canAccept,
        boolean canBlock,
        boolean canUnblock) {

    public UUID relationshipId() {
        return relationship.map(Relationship::getId).orElse(null);
    }
}

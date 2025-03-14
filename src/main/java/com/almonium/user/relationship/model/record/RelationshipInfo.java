package com.almonium.user.relationship.model.record;

import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import java.util.Optional;
import java.util.UUID;

public record RelationshipInfo(
        Optional<Relationship> friendship,
        RelativeRelationshipStatus status,
        UUID friendshipId,
        Boolean acceptsRequests,
        boolean profileVisible) {}

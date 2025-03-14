package com.almonium.user.friendship.model.record;

import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.RelationshipStatus;
import java.util.Optional;
import java.util.UUID;

public record RelationshipInfo(
        Optional<Friendship> friendship,
        RelationshipStatus status,
        UUID friendshipId,
        Boolean acceptsRequests,
        boolean profileVisible) {}

package com.almonium.user.relationship.service;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.exception.RelationshipException;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import com.almonium.user.relationship.model.record.RelationshipPerspective;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RelationshipPerspectiveResolver {

    public RelationshipPerspective resolve(
            UUID viewerId, User counterpart, Optional<Relationship> relationship, boolean profileHidden) {
        if (relationship.isEmpty()) {
            return stranger(counterpart, profileHidden);
        }

        Relationship existing = relationship.get();
        boolean viewerIsRequester = viewerId.equals(existing.getRequester().getId());
        boolean viewerIsRequestee = viewerId.equals(existing.getRequestee().getId());
        if (!viewerIsRequester && !viewerIsRequestee) {
            throw new RelationshipException("User is not part of this relationship");
        }

        User expectedCounterpart = viewerIsRequester ? existing.getRequestee() : existing.getRequester();
        if (!expectedCounterpart.getId().equals(counterpart.getId())) {
            throw new RelationshipException("Relationship does not match the requested profile");
        }

        return switch (existing.getStatus()) {
            case FRIENDS -> perspective(
                    existing, counterpart, RelativeRelationshipStatus.FRIENDS, null, true, false, false, true, false);
            case PENDING -> perspective(
                    existing,
                    counterpart,
                    viewerIsRequester
                            ? RelativeRelationshipStatus.PENDING_OUTGOING
                            : RelativeRelationshipStatus.PENDING_INCOMING,
                    null,
                    !profileHidden,
                    false,
                    viewerIsRequestee,
                    true,
                    false);
            case FST_BLOCKED_SND -> viewerIsRequester
                    ? blockedByViewer(existing, counterpart, profileHidden)
                    : blockedByCounterpart(existing, counterpart);
            case SND_BLOCKED_FST -> viewerIsRequestee
                    ? blockedByViewer(existing, counterpart, profileHidden)
                    : blockedByCounterpart(existing, counterpart);
            case MUTUAL_BLOCK -> perspective(
                    existing, counterpart, RelativeRelationshipStatus.BLOCKED, null, false, false, false, false, true);
            case REJECTED, CANCELLED, UNFRIENDED -> perspective(
                    existing,
                    counterpart,
                    RelativeRelationshipStatus.STRANGER,
                    !profileHidden,
                    !profileHidden,
                    !profileHidden,
                    false,
                    true,
                    false);
        };
    }

    public RelationshipPerspective resolve(User viewer, Relationship relationship) {
        User counterpart;
        if (viewer.equals(relationship.getRequester())) {
            counterpart = relationship.getRequestee();
        } else if (viewer.equals(relationship.getRequestee())) {
            counterpart = relationship.getRequester();
        } else {
            throw new RelationshipException("User is not part of this relationship");
        }

        return resolve(
                viewer.getId(),
                counterpart,
                Optional.of(relationship),
                counterpart.getProfile().isHidden());
    }

    public RelationshipPerspective stranger(User counterpart, boolean profileHidden) {
        return new RelationshipPerspective(
                Optional.empty(),
                counterpart,
                RelativeRelationshipStatus.STRANGER,
                !profileHidden,
                !profileHidden,
                !profileHidden,
                false,
                true,
                false);
    }

    private RelationshipPerspective blockedByViewer(
            Relationship relationship, User counterpart, boolean profileHidden) {
        return perspective(
                relationship,
                counterpart,
                RelativeRelationshipStatus.BLOCKED,
                null,
                !profileHidden,
                false,
                false,
                false,
                true);
    }

    private RelationshipPerspective blockedByCounterpart(Relationship relationship, User counterpart) {
        return perspective(
                relationship,
                counterpart,
                RelativeRelationshipStatus.STRANGER,
                false,
                false,
                false,
                false,
                true,
                false);
    }

    private RelationshipPerspective perspective(
            Relationship relationship,
            User counterpart,
            RelativeRelationshipStatus status,
            Boolean acceptsRequests,
            boolean profileVisible,
            boolean canRequest,
            boolean canAccept,
            boolean canBlock,
            boolean canUnblock) {
        return new RelationshipPerspective(
                Optional.of(relationship),
                counterpart,
                status,
                acceptsRequests,
                profileVisible,
                canRequest,
                canAccept,
                canBlock,
                canUnblock);
    }
}

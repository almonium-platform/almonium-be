package com.almonium.user.relationship.service;

import static com.almonium.user.relationship.model.enums.RelationshipStatus.CANCELLED;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FRIENDS;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.MUTUAL_BLOCK;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.PENDING;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.REJECTED;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.SND_BLOCKED_FST;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.UNFRIENDED;

import com.almonium.user.relationship.exception.RelationshipException;
import com.almonium.user.relationship.model.enums.RelationshipAction;
import com.almonium.user.relationship.model.enums.RelationshipStatus;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RelationshipStateMachine {
    private static final String RELATIONSHIP_IS_ALREADY_BLOCKED = "Relationship is already blocked";

    private static final Map<Transition, RelationshipStatus> SIMPLE_TRANSITIONS = Map.of(
            new Transition(PENDING, RelationshipAction.ACCEPT, ActorRole.REQUESTEE), FRIENDS,
            new Transition(PENDING, RelationshipAction.CANCEL, ActorRole.REQUESTER), CANCELLED,
            new Transition(PENDING, RelationshipAction.REJECT, ActorRole.REQUESTEE), REJECTED,
            new Transition(FRIENDS, RelationshipAction.UNFRIEND, ActorRole.REQUESTER), UNFRIENDED,
            new Transition(FRIENDS, RelationshipAction.UNFRIEND, ActorRole.REQUESTEE), UNFRIENDED);

    public RelationshipStatus transition(
            RelationshipStatus currentStatus, RelationshipAction action, ActorRole actorRole) {
        return switch (action) {
            case ACCEPT, CANCEL, REJECT, UNFRIEND -> simpleTransition(currentStatus, action, actorRole);
            case BLOCK -> block(currentStatus, actorRole);
            case UNBLOCK -> unblock(currentStatus, actorRole);
        };
    }

    private RelationshipStatus simpleTransition(
            RelationshipStatus currentStatus, RelationshipAction action, ActorRole actorRole) {
        RelationshipStatus nextStatus = SIMPLE_TRANSITIONS.get(new Transition(currentStatus, action, actorRole));
        if (nextStatus != null) {
            return nextStatus;
        }

        if (currentStatus == PENDING && action == RelationshipAction.CANCEL) {
            throw new RelationshipException("User is not the requester of this relationship");
        }
        if (currentStatus == PENDING && (action == RelationshipAction.ACCEPT || action == RelationshipAction.REJECT)) {
            throw new RelationshipException("User is not the requestee of this relationship");
        }
        throw new RelationshipException("Invalid relationship transition: " + currentStatus + " + " + action);
    }

    private RelationshipStatus block(RelationshipStatus currentStatus, ActorRole actorRole) {
        if (currentStatus == MUTUAL_BLOCK
                || currentStatus == FST_BLOCKED_SND && actorRole == ActorRole.REQUESTER
                || currentStatus == SND_BLOCKED_FST && actorRole == ActorRole.REQUESTEE) {
            throw new RelationshipException(RELATIONSHIP_IS_ALREADY_BLOCKED);
        }
        if (currentStatus == FST_BLOCKED_SND || currentStatus == SND_BLOCKED_FST) {
            return MUTUAL_BLOCK;
        }
        return actorRole == ActorRole.REQUESTER ? FST_BLOCKED_SND : SND_BLOCKED_FST;
    }

    private RelationshipStatus unblock(RelationshipStatus currentStatus, ActorRole actorRole) {
        if (currentStatus == MUTUAL_BLOCK) {
            return actorRole == ActorRole.REQUESTER ? SND_BLOCKED_FST : FST_BLOCKED_SND;
        }
        if (currentStatus == FST_BLOCKED_SND) {
            if (actorRole != ActorRole.REQUESTER) {
                throw new RelationshipException("User is not the denier of this relationship");
            }
            return UNFRIENDED;
        }
        if (currentStatus == SND_BLOCKED_FST) {
            if (actorRole != ActorRole.REQUESTEE) {
                throw new RelationshipException("User is not the denier of this relationship");
            }
            return UNFRIENDED;
        }
        throw new RelationshipException("Friendship is not blocked");
    }

    public enum ActorRole {
        REQUESTER,
        REQUESTEE
    }

    private record Transition(RelationshipStatus status, RelationshipAction action, ActorRole actorRole) {}
}

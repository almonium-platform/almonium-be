package com.almonium.user.relationship.service;

import static com.almonium.user.relationship.model.enums.RelationshipStatus.PENDING;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.service.NotificationService;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.relationship.dto.request.FriendshipRequestDto;
import com.almonium.user.relationship.dto.response.RelatedUserProfile;
import com.almonium.user.relationship.exception.RelationshipException;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipAction;
import com.almonium.user.relationship.model.enums.RelationshipStatus;
import com.almonium.user.relationship.model.projection.RelationshipToUserProjection;
import com.almonium.user.relationship.model.record.RelationshipPerspective;
import com.almonium.user.relationship.repository.RelationshipRepository;
import com.almonium.user.relationship.service.RelationshipStateMachine.ActorRole;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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
@Transactional(readOnly = true)
public class RelationshipService {
    private static final String RELATIONSHIP_CANT_BE_ESTABLISHED = "Couldn't create or re-establish relationship";
    private static final String RELATIONSHIP_NOT_FOUND = "Relationship not found";

    ProfileService profileService;
    NotificationService notificationService;

    RelationshipRepository relationshipRepository;
    RelationshipStateMachine stateMachine;
    RelationshipPerspectiveResolver perspectiveResolver;

    public List<RelatedUserProfile> findUsersByUsername(UUID id, String username) {
        return relationshipRepository.searchUsersByUsername(id, username);
    }

    public List<RelationshipToUserProjection> searchFriends(UUID id, String username) {
        return relationshipRepository.searchFriendsByUsername(id, username);
    }

    public List<RelatedUserProfile> getSentRequests(UUID id) {
        return relationshipRepository.getSentRequests(id);
    }

    public List<RelatedUserProfile> getReceivedRequests(UUID id) {
        return relationshipRepository.getReceivedRequests(id);
    }

    public List<RelatedUserProfile> getFriends(UUID id) {
        return relationshipRepository.getFriendships(id);
    }

    public List<RelatedUserProfile> getBlocked(UUID id) {
        return relationshipRepository.getBlocked(id);
    }

    /**
     * If no active relationship is established, we must return acceptsFriendRequests and profileVisible
     * You can see the friend's profile
     * If you're blocked by the profile, you can't neither see the profile nor send friend requests
     * If you blocked the profile unilaterally you can see the profile if it's public but can't send friend requests
     * If you blocked each other, you can't see the profile nor send friend requests
     * @param viewerId - the user who is viewing the profile
     * @param profileId - the profile that is being viewed
     * @param profileHidden - whether the profile is hidden
     * @return - the relationship info between the viewer and the profile
     */
    public RelationshipPerspective getRelationshipPerspective(UUID viewerId, User profileUser, boolean profileHidden) {
        var relationship = relationshipRepository.getRelationshipByUsersIds(viewerId, profileUser.getId());
        return perspectiveResolver.resolve(viewerId, profileUser, relationship, profileHidden);
    }

    @Transactional
    public Relationship createFriendshipRequest(User requester, FriendshipRequestDto dto) {
        validateDistinctUsers(requester.getId(), dto.recipientId());
        return relationshipRepository
                .getRelationshipByUsersIds(requester.getId(), dto.recipientId())
                .map(relationship -> reestablishFriendshipAndNotifyOrThrow(requester, relationship))
                .orElseGet(() -> createFriendshipAndNotify(requester, dto));
    }

    @Transactional
    public Relationship manageFriendship(User user, UUID id, RelationshipAction action) {
        Relationship relationship = relationshipRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(RELATIONSHIP_NOT_FOUND));
        ActorRole actorRole = actorRole(user, relationship);
        RelationshipStatus nextStatus = stateMachine.transition(relationship.getStatus(), action, actorRole);
        Relationship updatedRelationship = setStatusAndSave(relationship, nextStatus);
        if (action == RelationshipAction.ACCEPT) {
            notificationService.notifyOfFriendshipAcceptance(updatedRelationship);
        }
        return updatedRelationship;
    }

    @Transactional
    public void blockUser(User user, UUID targetUserId) {
        validateDistinctUsers(user.getId(), targetUserId);
        relationshipRepository
                .getRelationshipByUsersIds(user.getId(), targetUserId)
                .ifPresentOrElse(relationship -> applyBlock(user, relationship), () -> {
                    var relationship = new Relationship(
                            user, profileService.getProfileById(targetUserId).getUser());
                    relationshipRepository.save(relationship);
                    applyBlock(user, relationship);
                });
    }

    private Relationship createFriendshipAndNotify(User requester, FriendshipRequestDto dto) {
        Profile recipient = profileService.getProfileById(dto.recipientId());

        if (recipient.isHidden()) {
            throw new RelationshipException(RELATIONSHIP_CANT_BE_ESTABLISHED);
        }

        Relationship relationship = relationshipRepository.save(new Relationship(requester, recipient.getUser()));
        notifyAboutFriendshipRequestReceival(relationship);
        return relationship;
    }

    private Relationship reestablishFriendshipAndNotifyOrThrow(User requester, Relationship existingRelationship) {
        if (!existingRelationship.getStatus().isRetryable()) {
            throw new RelationshipException(RELATIONSHIP_CANT_BE_ESTABLISHED);
        }

        if (existingRelationship.getRequestee().equals(requester)) {
            existingRelationship.setRequestee(existingRelationship.getRequester());
            existingRelationship.setRequester(requester);
        }

        if (profileService
                .getProfileById(existingRelationship.getRequestee().getId())
                .isHidden()) {
            throw new RelationshipException(RELATIONSHIP_CANT_BE_ESTABLISHED);
        }

        setStatusAndSave(existingRelationship, PENDING);
        notifyAboutFriendshipRequestReceival(existingRelationship);
        return existingRelationship;
    }

    private void notifyAboutFriendshipRequestReceival(Relationship relationship) {
        notificationService.notifyFriendshipRequestRecipient(
                relationship.getRequester(), relationship.getRequestee(), relationship);
    }

    private Relationship setStatusAndSave(Relationship relationship, RelationshipStatus status) {
        relationship.setStatus(status);
        return relationshipRepository.save(relationship);
    }

    private Relationship applyBlock(User user, Relationship relationship) {
        ActorRole actorRole = actorRole(user, relationship);
        RelationshipStatus nextStatus =
                stateMachine.transition(relationship.getStatus(), RelationshipAction.BLOCK, actorRole);
        return setStatusAndSave(relationship, nextStatus);
    }

    private ActorRole actorRole(User user, Relationship relationship) {
        if (user.equals(relationship.getRequester())) {
            return ActorRole.REQUESTER;
        }
        if (user.equals(relationship.getRequestee())) {
            return ActorRole.REQUESTEE;
        }
        throw new RelationshipException("User is not part of this relationship");
    }

    private void validateDistinctUsers(UUID firstUserId, UUID secondUserId) {
        if (firstUserId.equals(secondUserId)) {
            throw new RelationshipException("A user cannot have a relationship with themselves");
        }
    }
}

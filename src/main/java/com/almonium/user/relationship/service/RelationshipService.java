package com.almonium.user.relationship.service;

import static com.almonium.user.relationship.model.enums.RelationshipStatus.CANCELLED;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FRIENDS;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.MUTUAL_BLOCK;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.PENDING;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.REJECTED;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.SND_BLOCKED_FST;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.UNFRIENDED;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.service.NotificationService;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.relationship.dto.request.FriendshipRequestDto;
import com.almonium.user.relationship.dto.response.PublicUserProfile;
import com.almonium.user.relationship.dto.response.RelatedUserProfile;
import com.almonium.user.relationship.exception.RelationshipException;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipAction;
import com.almonium.user.relationship.model.enums.RelationshipStatus;
import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import com.almonium.user.relationship.model.projection.RelationshipToUserProjection;
import com.almonium.user.relationship.model.record.RelationshipInfo;
import com.almonium.user.relationship.repository.RelationshipRepository;
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
    private static final String RELATIONSHIP_IS_ALREADY_BLOCKED = "Relationship is already blocked";
    private static final String RELATIONSHIP_NOT_FOUND = "Relationship not found";

    ProfileService profileService;
    NotificationService notificationService;

    RelationshipRepository relationshipRepository;

    public List<PublicUserProfile> findUsersByUsername(UUID id, String username) {
        return relationshipRepository.findNewFriendCandidates(id, username, RelationshipStatus.retryableStatuses());
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
    public RelationshipInfo getRelationshipInfo(UUID viewerId, UUID profileId, boolean profileHidden) {
        var friendshipOptional = relationshipRepository.getRelationshipByUsersIds(viewerId, profileId);

        RelativeRelationshipStatus status = RelativeRelationshipStatus.STRANGER;
        UUID friendshipId = null;

        Boolean acceptsFriendRequests = null; // only makes sense for STRANGER
        boolean profileVisible = !profileHidden; // always relevant

        if (friendshipOptional.isPresent()) {
            Relationship relationship = friendshipOptional.get();
            friendshipId = relationship.getId();
            boolean isRequester = viewerId.equals(relationship.getRequester().getId());

            switch (relationship.getStatus()) {
                case FRIENDS -> {
                    status = RelativeRelationshipStatus.FRIENDS;
                    profileVisible = true;
                }
                case PENDING ->
                    status = isRequester
                            ? RelativeRelationshipStatus.PENDING_OUTGOING
                            : RelativeRelationshipStatus.PENDING_INCOMING;
                case FST_BLOCKED_SND -> {
                    if (isRequester) {
                        status = RelativeRelationshipStatus.BLOCKED;
                    } else {
                        acceptsFriendRequests = false;
                        profileVisible = false;
                    }
                }
                case SND_BLOCKED_FST -> {
                    if (isRequester) {
                        acceptsFriendRequests = false;
                        profileVisible = false;
                    } else {
                        status = RelativeRelationshipStatus.BLOCKED;
                    }
                }
                case MUTUAL_BLOCK -> {
                    status = RelativeRelationshipStatus.BLOCKED;
                    profileVisible = false;
                }
                // same as if no relationship exists
                case REJECTED, CANCELLED, UNFRIENDED -> acceptsFriendRequests = profileHidden;
            }
        }

        return new RelationshipInfo(friendshipOptional, status, friendshipId, acceptsFriendRequests, profileVisible);
    }

    @Transactional
    public Relationship createFriendshipRequest(User requester, FriendshipRequestDto dto) {
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
        validateUserIsPartOfFriendship(user, relationship);

        return switch (action) {
            case ACCEPT -> befriend(user, relationship);
            case CANCEL -> cancelOwnRequest(user, relationship);
            case REJECT -> rejectIncomingRequest(user, relationship);
            case UNFRIEND -> unfriend(relationship);
            case BLOCK -> block(user, relationship);
            case UNBLOCK -> unblock(user, relationship);
        };
    }

    @Transactional
    public void blockUser(User user, UUID targetUserId) {
        relationshipRepository
                .getRelationshipByUsersIds(user.getId(), targetUserId)
                .ifPresentOrElse(relationship -> block(user, relationship), () -> {
                    var relationship = new Relationship(
                            user, profileService.getProfileById(targetUserId).getUser());
                    relationshipRepository.save(relationship);
                    block(user, relationship);
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

        setStatusAndSave(existingRelationship, PENDING);
        notifyAboutFriendshipRequestReceival(existingRelationship);
        return existingRelationship;
    }

    private void notifyAboutFriendshipRequestReceival(Relationship relationship) {
        notificationService.notifyFriendshipRequestRecipient(
                relationship.getRequester(), relationship.getRequestee(), relationship);
    }

    private Relationship befriend(User currentUser, Relationship relationship) {
        validateFriendshipStatus(relationship, PENDING);
        validateCorrectRole(currentUser, relationship, false);
        relationship.setStatus(FRIENDS);
        relationshipRepository.save(relationship);

        notificationService.notifyOfFriendshipAcceptance(relationship);
        return relationship;
    }

    private Relationship cancelOwnRequest(User user, Relationship relationship) {
        validateFriendshipStatus(relationship, PENDING);
        validateCorrectRole(user, relationship, true);
        return setStatusAndSave(relationship, CANCELLED);
    }

    private Relationship rejectIncomingRequest(User user, Relationship relationship) {
        validateFriendshipStatus(relationship, PENDING);
        validateCorrectRole(user, relationship, false);
        return setStatusAndSave(relationship, REJECTED);
    }

    private Relationship unfriend(Relationship relationship) {
        validateFriendshipStatus(relationship, FRIENDS);
        return setStatusAndSave(relationship, UNFRIENDED);
    }

    private Relationship setStatusAndSave(Relationship relationship, RelationshipStatus status) {
        relationship.setStatus(status);
        return relationshipRepository.save(relationship);
    }

    /**
     * You can block a relationship if it's not already blocked by you
     * If the other user has already blocked you, it becomes a mutual block
     */
    private Relationship block(User user, Relationship relationship) {
        if (relationship.getStatus() == MUTUAL_BLOCK) {
            throw new RelationshipException(RELATIONSHIP_IS_ALREADY_BLOCKED);
        }

        var friendshipDenier = relationship.getRelationshipDenier();
        boolean alreadyBlocked = friendshipDenier.isPresent();
        if (alreadyBlocked && friendshipDenier.get().equals(user.getId())) {
            throw new RelationshipException(RELATIONSHIP_IS_ALREADY_BLOCKED);
        }

        RelationshipStatus status = alreadyBlocked
                ? MUTUAL_BLOCK
                : user.equals(relationship.getRequester()) ? FST_BLOCKED_SND : SND_BLOCKED_FST;

        relationship.setStatus(status);
        return relationshipRepository.save(relationship);
    }

    private Relationship unblock(User user, Relationship relationship) {
        var friendshipDenier = relationship.getRelationshipDenier();
        boolean alreadyBlocked = friendshipDenier.isPresent();
        if (!alreadyBlocked) {
            throw new RelationshipException("Friendship is not blocked");
        }

        if (!friendshipDenier.get().equals(user.getId())) {
            throw new RelationshipException("User is not the denier of this relationship");
        }

        var status = relationship.getStatus() == MUTUAL_BLOCK
                ? user.equals(relationship.getRequester()) ? SND_BLOCKED_FST : FST_BLOCKED_SND
                : FRIENDS;

        return setStatusAndSave(relationship, status);
    }

    private void validateUserIsPartOfFriendship(User user, Relationship relationship) {
        if (!user.equals(relationship.getRequester()) && !user.equals(relationship.getRequestee())) {
            throw new RelationshipException("User is not part of this relationship");
        }
    }

    private void validateCorrectRole(User user, Relationship relationship, boolean requesterNotRequestee) {
        if (requesterNotRequestee && !user.equals(relationship.getRequester())) {
            throw new RelationshipException("User is not the requester of this relationship");
        }
        if (!requesterNotRequestee && !user.equals(relationship.getRequestee())) {
            throw new RelationshipException("User is not the requestee of this relationship");
        }
    }

    private void validateFriendshipStatus(Relationship relationship, RelationshipStatus... allowedStatuses) {
        if (!List.of(allowedStatuses).contains(relationship.getStatus())) {
            throw new RelationshipException("Friendship status must be one of " + List.of(allowedStatuses));
        }
    }
}

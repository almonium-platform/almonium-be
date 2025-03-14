package com.almonium.user.friendship.service;

import static com.almonium.user.friendship.model.enums.FriendshipStatus.CANCELLED;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.MUTUAL_BLOCK;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.REJECTED;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.UNFRIENDED;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.service.NotificationService;
import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.friendship.dto.request.FriendshipRequestDto;
import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.dto.response.RelatedUserProfile;
import com.almonium.user.friendship.exception.FriendshipException;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipAction;
import com.almonium.user.friendship.model.enums.FriendshipStatus;
import com.almonium.user.friendship.model.enums.RelationshipStatus;
import com.almonium.user.friendship.model.projection.FriendshipToUserProjection;
import com.almonium.user.friendship.model.record.RelationshipInfo;
import com.almonium.user.friendship.repository.FriendshipRepository;
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
public class FriendshipService {
    private static final String FRIENDSHIP_CANT_BE_ESTABLISHED = "Couldn't create or re-establish friendship";
    private static final String FRIENDSHIP_IS_ALREADY_BLOCKED = "Friendship is already blocked";
    private static final String FRIENDSHIP_NOT_FOUND = "Friendship not found";

    ProfileService profileService;
    NotificationService notificationService;

    FriendshipRepository friendshipRepository;

    public List<PublicUserProfile> findUsersByUsername(UUID id, String username) {
        return friendshipRepository.findNewFriendCandidates(id, username, FriendshipStatus.retryableStatuses());
    }

    public List<FriendshipToUserProjection> searchFriends(UUID id, String username) {
        return friendshipRepository.searchFriendsByUsername(id, username);
    }

    public List<RelatedUserProfile> getSentRequests(UUID id) {
        return friendshipRepository.getSentRequests(id);
    }

    public List<RelatedUserProfile> getReceivedRequests(UUID id) {
        return friendshipRepository.getReceivedRequests(id);
    }

    public List<RelatedUserProfile> getFriends(UUID id) {
        return friendshipRepository.getFriendships(id);
    }

    public List<RelatedUserProfile> getBlocked(UUID id) {
        return friendshipRepository.getBlocked(id);
    }

    /**
     * If no active friendship is established, we must return acceptsFriendRequests and profileVisible
     * You can see the friend's profile
     * If you're blocked by the profile, you can't neither see the profile nor send friend requests
     * If you blocked the profile unilaterally you can see the profile but can't send friend requests
     * If you blocked each other, you can't see the profile nor send friend requests
     * @param viewerId - the user who is viewing the profile
     * @param profileId - the profile that is being viewed
     * @param profileHidden - whether the profile is hidden
     * @return - the relationship info between the viewer and the profile
     */
    public RelationshipInfo getRelationshipInfo(UUID viewerId, UUID profileId, boolean profileHidden) {
        var friendshipOptional = friendshipRepository.getFriendshipByUsersIds(viewerId, profileId);

        RelationshipStatus status = RelationshipStatus.STRANGER;
        UUID friendshipId = null;

        Boolean acceptsFriendRequests = null; // only makes sense for STRANGER
        boolean profileVisible = !profileHidden; // always relevant

        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();
            friendshipId = friendship.getId();
            boolean isRequester = viewerId.equals(friendship.getRequester().getId());

            switch (friendship.getStatus()) {
                case FRIENDS -> {
                    status = RelationshipStatus.FRIENDS;
                    profileVisible = true;
                }
                case PENDING ->
                    status = isRequester ? RelationshipStatus.PENDING_OUTGOING : RelationshipStatus.PENDING_INCOMING;
                case FST_BLOCKED_SND -> {
                    if (isRequester) {
                        status = RelationshipStatus.BLOCKED;
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
                        status = RelationshipStatus.BLOCKED;
                    }
                }
                case MUTUAL_BLOCK -> {
                    status = RelationshipStatus.BLOCKED;
                    profileVisible = false;
                }
                // same as if no friendship exists
                case REJECTED, CANCELLED, UNFRIENDED -> acceptsFriendRequests = profileHidden;
            }
        }

        return new RelationshipInfo(friendshipOptional, status, friendshipId, acceptsFriendRequests, profileVisible);
    }

    @Transactional
    public Friendship createFriendshipRequest(User requester, FriendshipRequestDto dto) {
        return friendshipRepository
                .getFriendshipByUsersIds(requester.getId(), dto.recipientId())
                .map(friendship -> reestablishFriendshipAndNotifyOrThrow(requester, friendship))
                .orElseGet(() -> createFriendshipAndNotify(requester, dto));
    }

    @Transactional
    public Friendship manageFriendship(User user, UUID id, FriendshipAction action) {
        Friendship friendship =
                friendshipRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(FRIENDSHIP_NOT_FOUND));
        validateUserIsPartOfFriendship(user, friendship);

        return switch (action) {
            case ACCEPT -> befriend(user, friendship);
            case CANCEL -> cancelOwnRequest(user, friendship);
            case REJECT -> rejectIncomingRequest(user, friendship);
            case UNFRIEND -> unfriend(friendship);
            case BLOCK -> block(user, friendship);
            case UNBLOCK -> unblock(user, friendship);
        };
    }

    @Transactional
    public void blockUser(User user, UUID targetUserId) {
        friendshipRepository
                .getFriendshipByUsersIds(user.getId(), targetUserId)
                .ifPresentOrElse(friendship -> block(user, friendship), () -> {
                    var friendship = new Friendship(
                            user, profileService.getProfileById(targetUserId).getUser());
                    friendshipRepository.save(friendship);
                    block(user, friendship);
                });
    }

    private Friendship createFriendshipAndNotify(User requester, FriendshipRequestDto dto) {
        Profile recipient = profileService.getProfileById(dto.recipientId());

        if (recipient.isHidden()) {
            throw new FriendshipException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }

        Friendship friendship = friendshipRepository.save(new Friendship(requester, recipient.getUser()));
        notifyAboutFriendshipRequestReceival(friendship);
        return friendship;
    }

    private Friendship reestablishFriendshipAndNotifyOrThrow(User requester, Friendship existingFriendship) {
        if (!existingFriendship.getStatus().isRetryable()) {
            throw new FriendshipException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }

        if (existingFriendship.getRequestee().equals(requester)) {
            existingFriendship.setRequestee(existingFriendship.getRequester());
            existingFriendship.setRequester(requester);
        }

        setStatusAndSave(existingFriendship, PENDING);
        notifyAboutFriendshipRequestReceival(existingFriendship);
        return existingFriendship;
    }

    private void notifyAboutFriendshipRequestReceival(Friendship friendship) {
        notificationService.notifyFriendshipRequestRecipient(
                friendship.getRequester(), friendship.getRequestee(), friendship);
    }

    private Friendship befriend(User currentUser, Friendship friendship) {
        validateFriendshipStatus(friendship, PENDING);
        validateCorrectRole(currentUser, friendship, false);
        friendship.setStatus(FRIENDS);
        friendshipRepository.save(friendship);

        notificationService.notifyOfFriendshipAcceptance(friendship);
        return friendship;
    }

    private Friendship cancelOwnRequest(User user, Friendship friendship) {
        validateFriendshipStatus(friendship, PENDING);
        validateCorrectRole(user, friendship, true);
        return setStatusAndSave(friendship, CANCELLED);
    }

    private Friendship rejectIncomingRequest(User user, Friendship friendship) {
        validateFriendshipStatus(friendship, PENDING);
        validateCorrectRole(user, friendship, false);
        return setStatusAndSave(friendship, REJECTED);
    }

    private Friendship unfriend(Friendship friendship) {
        validateFriendshipStatus(friendship, FRIENDS);
        return setStatusAndSave(friendship, UNFRIENDED);
    }

    private Friendship setStatusAndSave(Friendship friendship, FriendshipStatus status) {
        friendship.setStatus(status);
        return friendshipRepository.save(friendship);
    }

    /**
     * You can block a friendship if it's not already blocked by you
     * If the other user has already blocked you, it becomes a mutual block
     */
    private Friendship block(User user, Friendship friendship) {
        var friendshipDenier = friendship.getFriendshipDenier();
        boolean alreadyBlocked = friendshipDenier.isPresent();
        if (alreadyBlocked && friendshipDenier.get().equals(user.getId())) {
            throw new FriendshipException(FRIENDSHIP_IS_ALREADY_BLOCKED);
        }

        FriendshipStatus status = alreadyBlocked
                ? MUTUAL_BLOCK
                : user.equals(friendship.getRequester()) ? FST_BLOCKED_SND : SND_BLOCKED_FST;

        friendship.setStatus(status);
        return friendshipRepository.save(friendship);
    }

    private Friendship unblock(User user, Friendship friendship) {
        var friendshipDenier = friendship.getFriendshipDenier();
        boolean alreadyBlocked = friendshipDenier.isPresent();
        if (!alreadyBlocked) {
            throw new FriendshipException("Friendship is not blocked");
        }

        if (!friendshipDenier.get().equals(user.getId())) {
            throw new FriendshipException("User is not the denier of this friendship");
        }

        var status = friendship.getStatus() == MUTUAL_BLOCK
                ? user.equals(friendship.getRequester()) ? SND_BLOCKED_FST : FST_BLOCKED_SND
                : FRIENDS;

        return setStatusAndSave(friendship, status);
    }

    private void validateUserIsPartOfFriendship(User user, Friendship friendship) {
        if (!user.equals(friendship.getRequester()) && !user.equals(friendship.getRequestee())) {
            throw new FriendshipException("User is not part of this friendship");
        }
    }

    private void validateCorrectRole(User user, Friendship friendship, boolean requesterNotRequestee) {
        if (requesterNotRequestee && !user.equals(friendship.getRequester())) {
            throw new FriendshipException("User is not the requester of this friendship");
        }
        if (!requesterNotRequestee && !user.equals(friendship.getRequestee())) {
            throw new FriendshipException("User is not the requestee of this friendship");
        }
    }

    private void validateFriendshipStatus(Friendship friendship, FriendshipStatus... allowedStatuses) {
        if (!List.of(allowedStatuses).contains(friendship.getStatus())) {
            throw new FriendshipException("Friendship status must be one of " + List.of(allowedStatuses));
        }
    }
}

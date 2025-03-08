package com.almonium.user.friendship.service;

import static com.almonium.user.friendship.model.enums.FriendshipStatus.CANCELLED;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.REJECTED;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.UNFRIENDED;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.infra.notification.service.NotificationService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import com.almonium.user.friendship.dto.request.FriendshipRequestDto;
import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.dto.response.RelatedUserProfile;
import com.almonium.user.friendship.exception.FriendshipException;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipAction;
import com.almonium.user.friendship.model.enums.FriendshipStatus;
import com.almonium.user.friendship.model.projection.FriendshipToUserProjection;
import com.almonium.user.friendship.repository.FriendshipRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class FriendshipService {
    private static final String FRIENDSHIP_CANT_BE_ESTABLISHED = "Couldn't create or re-establish friendship";
    private static final String FRIENDSHIP_IS_ALREADY_BLOCKED = "Friendship is already blocked";
    private static final String FRIENDSHIP_NOT_FOUND = "Friendship not found";

    UserService userService;
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

    private Friendship createFriendshipAndNotify(User requester, FriendshipRequestDto dto) {
        User recipient = userService.getById(dto.recipientId());

        if (recipient.getProfile().isHidden()) {
            throw new FriendshipException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }

        Friendship friendship = friendshipRepository.save(new Friendship(requester, recipient));
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

    private Friendship block(User user, Friendship friendship) {
        var friendshipDenier = friendship.getFriendshipDenier();
        if (friendshipDenier.isPresent()) {
            if (friendshipDenier.get().equals(user.getId())) {
                throw new FriendshipException(FRIENDSHIP_IS_ALREADY_BLOCKED);
            }
            throw new FriendshipException(FRIENDSHIP_NOT_FOUND);
        } else {
            validateFriendshipStatus(friendship, FRIENDS, PENDING);
            friendship.setStatus(user.equals(friendship.getRequester()) ? FST_BLOCKED_SND : SND_BLOCKED_FST);
        }
        return friendshipRepository.save(friendship);
    }

    private Friendship unblock(User user, Friendship friendship) {
        var friendshipDenier = friendship.getFriendshipDenier();
        if (friendshipDenier.isEmpty()) {
            throw new FriendshipException("Friendship is not blocked");
        }
        if (!friendshipDenier.get().equals(user.getId())) {
            throw new FriendshipException("User is not the denier of this friendship");
        }

        return setStatusAndSave(friendship, FRIENDS);
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

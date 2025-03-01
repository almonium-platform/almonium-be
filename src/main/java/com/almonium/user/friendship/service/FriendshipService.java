package com.almonium.user.friendship.service;

import static com.almonium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class FriendshipService {
    private static final String FRIENDSHIP_CANT_BE_ESTABLISHED = "Couldn't create friendship request";
    private static final String FRIENDSHIP_IS_ALREADY_BLOCKED = "Friendship is already blocked";
    private static final String FRIENDSHIP_NOT_FOUND = "Friendship not found";

    UserService userService;
    NotificationService notificationService;
    FriendshipRepository friendshipRepository;

    public List<PublicUserProfile> findUsersByUsername(long id, String username) {
        return friendshipRepository.findNewFriendCandidates(id, username);
    }

    public List<FriendshipToUserProjection> searchFriends(long id, String username) {
        return friendshipRepository.searchFriendsByUsername(id, username);
    }

    public List<RelatedUserProfile> getSentRequests(long id) {
        return friendshipRepository.getSentRequests(id);
    }

    public List<RelatedUserProfile> getReceivedRequests(long id) {
        return friendshipRepository.getReceivedRequests(id);
    }

    public List<RelatedUserProfile> getFriends(long id) {
        return friendshipRepository.getFriendships(id);
    }

    public List<RelatedUserProfile> getBlocked(long id) {
        return friendshipRepository.getBlocked(id);
    }

    @Transactional
    public Friendship createFriendshipRequest(User user, FriendshipRequestDto dto) {
        Optional<Friendship> friendshipOptional =
                friendshipRepository.getFriendshipByUsersIds(user.getId(), dto.recipientId());
        if (friendshipOptional.isPresent()) {
            throw new FriendshipException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }

        User recipient = userService.getById(dto.recipientId());
        if (recipient.getProfile().isHidden()) {
            throw new FriendshipException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }

        Friendship friendship = friendshipRepository.save(new Friendship(user, recipient));

        notificationService.notifyFriendshipRequestRecipient(user, recipient, friendship);

        return friendship;
    }

    @Transactional
    public Friendship manageFriendship(User user, Long id, FriendshipAction action) {
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
        return deleteFriendship(friendship);
    }

    private Friendship rejectIncomingRequest(User user, Friendship friendship) {
        validateFriendshipStatus(friendship, PENDING);
        validateCorrectRole(user, friendship, false);
        return deleteFriendship(friendship);
    }

    private Friendship unfriend(Friendship friendship) {
        validateFriendshipStatus(friendship, FRIENDS);
        return deleteFriendship(friendship);
    }

    private Friendship block(User user, Friendship friendship) {
        Optional<Long> friendshipDenier = friendship.getFriendshipDenier();
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
        Optional<Long> friendshipDenier = friendship.getFriendshipDenier();
        if (friendshipDenier.isEmpty()) {
            throw new FriendshipException("Friendship is not blocked");
        }
        if (!friendshipDenier.get().equals(user.getId())) {
            throw new FriendshipException("User is not the denier of this friendship");
        }

        friendship.setStatus(FRIENDS);
        return friendshipRepository.save(friendship);
    }

    private Friendship deleteFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
        return friendship;
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

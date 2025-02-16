package com.almonium.user.friendship.service;

import static com.almonium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.MUTUALLY_BLOCKED;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
import static lombok.AccessLevel.PRIVATE;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.UserService;
import com.almonium.user.friendship.dto.request.FriendshipRequestDto;
import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.dto.response.RelatedUserProfile;
import com.almonium.user.friendship.exception.FriendshipNotAllowedException;
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

    UserService userService;
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
            throw new FriendshipNotAllowedException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }
        User recipient = userService.getById(dto.recipientId());
        if (recipient.getProfile().isHidden()) {
            throw new FriendshipNotAllowedException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }
        return friendshipRepository.save(new Friendship(user, recipient));
    }

    @Transactional
    public Friendship manageFriendship(User user, Long id, FriendshipAction action) {
        Friendship friendship = friendshipRepository.findById(id).orElseThrow(EntityNotFoundException::new);
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

    private Friendship befriend(User user, Friendship friendship) {
        validateFriendshipStatus(friendship, PENDING);
        validateCorrectRole(user, friendship, false);
        friendship.setStatus(FRIENDS);
        return friendshipRepository.save(friendship);
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
        if (friendship.getStatus() == MUTUALLY_BLOCKED) {
            throw new FriendshipNotAllowedException(FRIENDSHIP_IS_ALREADY_BLOCKED);
        }
        Optional<Long> friendshipDenier = friendship.getFriendshipDenier();
        if (friendshipDenier.isPresent()) {
            if (friendshipDenier.get().equals(user.getId())) {
                throw new FriendshipNotAllowedException(FRIENDSHIP_IS_ALREADY_BLOCKED);
            }
            friendship.setStatus(MUTUALLY_BLOCKED);
        } else {
            validateFriendshipStatus(friendship, FRIENDS, PENDING);
            friendship.setStatus(user.equals(friendship.getRequester()) ? FST_BLOCKED_SND : SND_BLOCKED_FST);
        }
        return friendshipRepository.save(friendship);
    }

    private Friendship unblock(User user, Friendship friendship) {
        if (friendship.getStatus() == MUTUALLY_BLOCKED) {
            friendship.setStatus(user.equals(friendship.getRequester()) ? SND_BLOCKED_FST : FST_BLOCKED_SND);
            return friendshipRepository.save(friendship);
        }

        Optional<Long> friendshipDenier = friendship.getFriendshipDenier();
        if (friendshipDenier.isEmpty()) {
            throw new FriendshipNotAllowedException("Friendship is not blocked");
        }
        if (!friendshipDenier.get().equals(user.getId())) {
            throw new FriendshipNotAllowedException("User is not the denier of this friendship");
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
            throw new FriendshipNotAllowedException("User is not part of this friendship");
        }
    }

    private void validateCorrectRole(User user, Friendship friendship, boolean requesterNotRequestee) {
        if (requesterNotRequestee && !user.equals(friendship.getRequester())) {
            throw new FriendshipNotAllowedException("User is not the requester of this friendship");
        }
        if (!requesterNotRequestee && !user.equals(friendship.getRequestee())) {
            throw new FriendshipNotAllowedException("User is not the requestee of this friendship");
        }
    }

    private void validateFriendshipStatus(Friendship friendship, FriendshipStatus... allowedStatuses) {
        if (!List.of(allowedStatuses).contains(friendship.getStatus())) {
            throw new FriendshipNotAllowedException("Friendship status must be one of " + List.of(allowedStatuses));
        }
    }
}

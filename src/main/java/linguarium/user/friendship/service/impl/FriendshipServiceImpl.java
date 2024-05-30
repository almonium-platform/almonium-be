package linguarium.user.friendship.service.impl;

import static linguarium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static linguarium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static linguarium.user.friendship.model.enums.FriendshipStatus.MUTUALLY_BLOCKED;
import static linguarium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static linguarium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
import static lombok.AccessLevel.PRIVATE;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import linguarium.user.core.service.UserService;
import linguarium.user.friendship.dto.FriendDto;
import linguarium.user.friendship.dto.FriendshipRequestDto;
import linguarium.user.friendship.exception.FriendshipNotAllowedException;
import linguarium.user.friendship.model.entity.Friendship;
import linguarium.user.friendship.model.enums.FriendshipAction;
import linguarium.user.friendship.model.enums.FriendshipStatus;
import linguarium.user.friendship.model.projection.FriendshipToUserProjection;
import linguarium.user.friendship.model.projection.UserToFriendProjection;
import linguarium.user.friendship.repository.FriendshipRepository;
import linguarium.user.friendship.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class FriendshipServiceImpl implements FriendshipService {
    private static final String FRIENDSHIP_CANT_BE_ESTABLISHED = "Couldn't create friendship request";
    private static final String FRIENDSHIP_IS_ALREADY_BLOCKED = "Friendship is already blocked";

    FriendshipRepository friendshipRepository;
    UserRepository userRepository;
    UserService userService;

    @Override
    public Optional<FriendDto> findFriendByEmail(String email) {
        Optional<UserToFriendProjection> friendOptional = userRepository.findFriendByEmail(email);
        return friendOptional.map(FriendDto::new);
    }

    @Override
    public List<FriendDto> getFriends(long id) {
        List<FriendshipToUserProjection> friendshipToUserProjections = friendshipRepository.getVisibleFriendships(id);
        List<FriendDto> result = new ArrayList<>();
        for (FriendshipToUserProjection friend : friendshipToUserProjections) {
            Optional<UserToFriendProjection> friendProjectionOptional = userRepository.findUserById(friend.getUserId());
            friendProjectionOptional.ifPresent(userToFriendProjection ->
                    result.add(new FriendDto(userToFriendProjection, friend.getStatus(), friend.isRequester())));
        }
        return result;
    }

    @Override
    @Transactional
    public Friendship createFriendshipRequest(User user, FriendshipRequestDto dto) {
        Optional<Friendship> friendshipOptional =
                friendshipRepository.getFriendshipByUsersIds(user.getId(), dto.recipientId());
        if (friendshipOptional.isPresent()) {
            throw new FriendshipNotAllowedException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }
        User recipient = userService.getById(dto.recipientId());
        if (recipient.getProfile().isFriendshipRequestsBlocked()) {
            throw new FriendshipNotAllowedException(FRIENDSHIP_CANT_BE_ESTABLISHED);
        }
        return friendshipRepository.save(new Friendship(user, recipient));
    }

    @Override
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
        return deleteFriendship(friendship);
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

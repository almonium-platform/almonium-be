package com.linguarium.friendship.service.impl;

import static com.linguarium.friendship.model.FriendshipStatus.FRIENDS;
import static com.linguarium.friendship.model.FriendshipStatus.FST_BLOCKED_SND;
import static com.linguarium.friendship.model.FriendshipStatus.MUTUALLY_BLOCKED;
import static com.linguarium.friendship.model.FriendshipStatus.PENDING;
import static com.linguarium.friendship.model.FriendshipStatus.SND_BLOCKED_FST;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.dto.FriendshipRequestDto;
import com.linguarium.friendship.exception.FriendshipNotAllowedException;
import com.linguarium.friendship.exception.FriendshipNotFoundException;
import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.FriendWrapper;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipAction;
import com.linguarium.friendship.model.FriendshipStatus;
import com.linguarium.friendship.repository.FriendshipRepository;
import com.linguarium.friendship.service.FriendshipService;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import com.linguarium.user.service.UserService;
import java.util.ArrayList;
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
public class FriendshipServiceImpl implements FriendshipService {
    private static final String USER_DOESNT_ACCEPT_REQUESTS_EX = "User doesn't accept friendship requests!";
    private static final String FRIENDSHIP_IS_ALREADY_BLOCKED = "Friendship is already blocked";

    FriendshipRepository friendshipRepository;
    UserRepository userRepository;
    UserService userService;

    @Override
    public Optional<FriendshipInfoDto> findFriendByEmail(final String email) {
        Optional<FriendWrapper> friendOptional = userRepository.findFriendByEmail(email);
        return friendOptional.map(FriendshipInfoDto::new);
    }

    @Override
    public List<FriendshipInfoDto> getFriendships(long id) {
        List<FriendInfoView> friendInfoViews = friendshipRepository.findByUserId(id);
        List<FriendshipInfoDto> result = new ArrayList<>();
        for (FriendInfoView view : friendInfoViews) {
            Optional<FriendWrapper> friendOptional = userRepository.findUserById(view.getUserId());
            friendOptional.ifPresent(friendWrapper -> result.add(new FriendshipInfoDto(
                    friendWrapper, FriendshipStatus.fromString(view.getStatus()), view.getIsFriendRequester())));
        }
        return result;
    }

    @Override
    @Transactional
    public Friendship manageFriendship(User user, Long id, FriendshipAction action) {
        Friendship friendship = friendshipRepository.findById(id).orElseThrow(FriendshipNotFoundException::new);
        validateUserIsPartOfFriendship(user, friendship);

        return switch (action) {
            case ACCEPT -> befriend(user, friendship);
            case BLOCK -> block(user, friendship);
            case UNBLOCK -> unblock(user, friendship);
            case CANCEL -> cancelOwnRequest(user, friendship);
            case REJECT -> rejectIncomingRequest(user, friendship);
            case UNFRIEND -> unfriend(friendship);
        };
    }

    @Override
    @Transactional
    public Friendship createFriendshipRequest(User user, FriendshipRequestDto dto) {
        Optional<Friendship> friendshipOptional =
                friendshipRepository.getFriendshipByUsersIds(user.getId(), dto.recipientId());
        if (friendshipOptional.isPresent()) {
            throw new FriendshipNotAllowedException("Couldn't create friendship request");
        }
        User recipient = userService.getById(dto.recipientId());
        if (recipient.getProfile().isFriendshipRequestsBlocked()) {
            throw new FriendshipNotAllowedException(USER_DOESNT_ACCEPT_REQUESTS_EX);
        }
        return friendshipRepository.save(new Friendship(user, recipient));
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

    private Friendship unblock(User user, Friendship friendship) {
        if (friendship.getStatus() == MUTUALLY_BLOCKED) {
            friendship.setStatus(user.getId().equals(friendship.getRequesterId()) ? SND_BLOCKED_FST : FST_BLOCKED_SND);
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

    private void validateUserIsPartOfFriendship(User user, Friendship friendship) {
        if (!user.getId().equals(friendship.getRequesterId()) && !user.getId().equals(friendship.getRequesteeId())) {
            throw new FriendshipNotAllowedException("User is not part of this friendship");
        }
    }

    private Friendship befriend(User user, Friendship friendship) {
        validateFriendshipStatus(friendship, PENDING);
        validateCorrectRole(user, friendship, false);
        friendship.setStatus(FRIENDS);
        return friendshipRepository.save(friendship);
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
            friendship.setStatus(user.getId().equals(friendship.getRequesterId()) ? FST_BLOCKED_SND : SND_BLOCKED_FST);
        }
        return friendshipRepository.save(friendship);
    }

    private Friendship unfriend(Friendship friendship) {
        validateFriendshipStatus(friendship, FRIENDS);
        return deleteFriendship(friendship);
    }

    private Friendship deleteFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
        return friendship;
    }

    private void validateCorrectRole(User user, Friendship friendship, boolean requesterNotRequestee) {
        if (requesterNotRequestee && !user.getId().equals(friendship.getRequesterId())) {
            throw new FriendshipNotAllowedException("User is not the requester of this friendship");
        }
        if (!requesterNotRequestee && !user.getId().equals(friendship.getRequesteeId())) {
            throw new FriendshipNotAllowedException("User is not the requestee of this friendship");
        }
    }

    private void validateFriendshipStatus(Friendship friendship, FriendshipStatus... allowedStatuses) {
        if (!List.of(allowedStatuses).contains(friendship.getStatus())) {
            throw new FriendshipNotAllowedException("Friendship status must be one of " + List.of(allowedStatuses));
        }
    }
}

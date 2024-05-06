package com.linguarium.friendship.service.impl;

import static com.linguarium.friendship.model.FriendshipStatus.FRIENDS;
import static com.linguarium.friendship.model.FriendshipStatus.PENDING;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.exception.FriendshipNotAllowedException;
import com.linguarium.friendship.exception.FriendshipNotFoundException;
import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.FriendWrapper;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipStatus;
import com.linguarium.friendship.repository.FriendshipRepository;
import com.linguarium.friendship.service.FriendshipService;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipServiceImpl implements FriendshipService {
    FriendshipRepository friendshipRepository;
    UserRepository userRepository;

    @Override
    public Optional<FriendshipInfoDto> findFriendByEmail(final String email) {
        Optional<FriendWrapper> friendOptional = userRepository.findFriendByEmail(email);
        return friendOptional.map(FriendshipInfoDto::new);
    }

    @Override
    public List<FriendshipInfoDto> getFriends(long id) {
        List<FriendInfoView> friendInfoViews = friendshipRepository.findByUserId(id);

        List<FriendshipInfoDto> result = new ArrayList<>();
        for (FriendInfoView view : friendInfoViews) {
            Optional<FriendWrapper> friendOptional = userRepository.findAllById(view.getUserId());
            friendOptional.ifPresent(friendWrapper -> result.add(new FriendshipInfoDto(
                    friendWrapper, FriendshipStatus.fromString(view.getStatus()), view.getIsFriendRequester())));
        }
        return result;
    }

    @Override
    @Transactional
    public Friendship manageFriendship(FriendshipActionDto dto) {
        return switch (dto.getAction()) {
            case REQUEST -> createFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
            case ACCEPT -> approveFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
            case BLOCK -> blockUser(dto.getIdInitiator(), dto.getIdAcceptor());
            case UNBLOCK, CANCEL, REJECT, UNFRIEND -> cancelFriendship(dto.getIdInitiator(), dto.getIdAcceptor());
        };
    }

    @SneakyThrows
    private Friendship blockUser(long actionInitiatorId, long actionAcceptorId) {
        Friendship friendship = friendshipRepository
                .getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.getStatus().equals(FRIENDS) && !friendship.getStatus().equals(PENDING)) {
            throw new IllegalArgumentException("Friendship status must be FRIENDS or PENDING");
        }

        LocalDateTime now = LocalDateTime.now();
        friendship.setUpdated(now);
        friendship.setStatus(
                friendship.getRequesterId().equals(actionInitiatorId)
                        ? FriendshipStatus.FST_BLOCKED_SND
                        : FriendshipStatus.SND_BLOCKED_FST);
        return friendshipRepository.save(friendship);
    }

    @SneakyThrows
    private Friendship cancelFriendship(Long actionInitiatorId, Long actionAcceptorId) {
        Friendship friendship = friendshipRepository
                .getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId)
                .orElseThrow(FriendshipNotFoundException::new);

        friendshipRepository.delete(friendship);
        return friendship;
    }

    @SneakyThrows
    private Friendship createFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        User recipient = userRepository.findById(actionAcceptorId).orElseThrow();

        Optional<Friendship> friendshipOptional =
                friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);

        if (friendshipOptional.isPresent()) {
            Friendship existingFriendship = friendshipOptional.get();

            if (Long.valueOf(actionAcceptorId).equals(existingFriendship.whoDeniesFriendship())) {
                throw new FriendshipNotAllowedException("User limited your ability to send requests!");
            }

            throw new FriendshipNotAllowedException(String.format(
                    "Friendship between %s and %s already exists, status: %s",
                    actionInitiatorId, actionAcceptorId, existingFriendship.getStatus()));
        }

        if (recipient.getProfile().isFriendshipRequestsBlocked()) {
            throw new FriendshipNotAllowedException("User doesn't accept friendship requests!");
        }

        Friendship friendship = Friendship.builder()
                .status(PENDING)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .requesterId(actionInitiatorId)
                .requesteeId(actionAcceptorId)
                .build();
        return friendshipRepository.save(friendship);
    }

    @SneakyThrows
    private Friendship approveFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        Friendship friendship = friendshipRepository
                .getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.getStatus().equals(PENDING)) {
            throw new IllegalArgumentException("Status must be PENDING");
        }

        friendship.setStatus(FriendshipStatus.FRIENDS);
        friendship.setUpdated(LocalDateTime.now());
        return friendshipRepository.save(friendship);
    }
}

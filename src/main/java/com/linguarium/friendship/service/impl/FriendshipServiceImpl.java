package com.linguarium.friendship.service.impl;

import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
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
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.linguarium.friendship.model.FriendshipStatus.FRIENDS;
import static com.linguarium.friendship.model.FriendshipStatus.PENDING;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipServiceImpl implements FriendshipService {
    FriendshipRepository friendshipRepository;
    UserRepository userRepository;

    @Override
    public Optional<FriendInfoDto> findFriendByEmail(final String email) {
        Optional<FriendWrapper> friendOptional = userRepository.findFriendByEmail(email);
        return friendOptional.map(FriendInfoDto::new);
    }

    @Override
    public Collection<FriendInfoDto> getFriends(long id) {
        List<FriendInfoView> friendInfoViews = friendshipRepository.findByUserId(id);

        List<FriendInfoDto> result = new ArrayList<>();
        for (FriendInfoView view : friendInfoViews) {
            Optional<FriendWrapper> friendOptional = userRepository.findAllById(view.getUserId());
            friendOptional.ifPresent(friendWrapper -> result.add(
                    new FriendInfoDto(
                            friendWrapper,
                            FriendshipStatus.fromString(view.getStatus()),
                            view.getIsFriendRequester())));
        }
        return result;
    }

    @Override
    @Transactional
    public Friendship editFriendship(FriendshipActionDto dto) {
        switch (dto.getAction()) {
            case REQUEST:
                return createFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
            case ACCEPT:
                return approveFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
            case BLOCK:
                return blockUser(dto.getIdInitiator(), dto.getIdAcceptor());
            case UNBLOCK:
            case CANCEL:
            case REJECT:
            case UNFRIEND:
                return cancelFriendship(dto.getIdInitiator(), dto.getIdAcceptor());
            default:
                throw new IllegalArgumentException();
        }
    }

    @SneakyThrows
    private Friendship blockUser(long actionInitiatorId, long actionAcceptorId) {
        Friendship friendship = friendshipRepository
                .getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId)
                .orElseThrow(FriendshipNotFoundException::new);

        if (!friendship.getFriendshipStatus().equals(FRIENDS) && !friendship.getFriendshipStatus().equals(PENDING)) {
            throw new IllegalArgumentException("Friendship status must be FRIENDS or PENDING");
        }

        LocalDateTime now = LocalDateTime.now();
        friendship.setUpdated(now);
        friendship.setFriendshipStatus(friendship.getRequesterId().equals(actionInitiatorId)
                ? FriendshipStatus.FST_BLOCKED_SND
                : FriendshipStatus.SND_BLOCKED_FST
        );
        return friendshipRepository.save(friendship);
    }

    @SneakyThrows
    private Friendship cancelFriendship(Long actionInitiatorId, Long actionAcceptorId) {
        Friendship friendship = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId)
                .orElseThrow(FriendshipNotFoundException::new);

        friendshipRepository.delete(friendship);
        return friendship;
    }

    @SneakyThrows
    private Friendship createFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        User recipient = userRepository.getById(actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        if (friendshipOptional.isPresent()) {
            Friendship existingFriendship = friendshipOptional.get();

            if (Long.valueOf(actionAcceptorId).equals(existingFriendship.whoDeniesFriendship())) {
                throw new FriendshipNotAllowedException("User limited your ability to send requests!");
            }

            throw new FriendshipNotAllowedException(String.format("Friendship between %s and %s already exists, status: %s",
                    actionInitiatorId, actionAcceptorId, existingFriendship.getFriendshipStatus()));
        }
        if (recipient.getProfile().isFriendshipRequestsBlocked()) {
            throw new FriendshipNotAllowedException("User doesn't accept friendship requests!");
        }

        Friendship friendship = Friendship.builder()
                .friendshipStatus(PENDING)
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

        if (!friendship.getFriendshipStatus().equals(PENDING)) {
            throw new IllegalArgumentException("Status must be PENDING");
        }

        friendship.setFriendshipStatus(FriendshipStatus.FRIENDS);
        friendship.setUpdated(LocalDateTime.now());
        return friendshipRepository.save(friendship);
    }
}

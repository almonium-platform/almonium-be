package com.linguatool.service;

import com.linguatool.exception.friend.FriendshipNotAllowedException;
import com.linguatool.exception.friend.FriendshipNotFoundException;
import com.linguatool.model.dto.Friend;
import com.linguatool.model.dto.FriendInfo;
import com.linguatool.model.dto.FriendshipCommandDto;
import com.linguatool.model.entity.user.Friendship;
import com.linguatool.model.entity.user.FriendshipStatus;
import com.linguatool.model.entity.user.User;
import com.linguatool.repository.FriendshipRepository;
import com.linguatool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.linguatool.model.entity.user.FriendshipStatus.FRIENDS;
import static com.linguatool.model.entity.user.FriendshipStatus.PENDING;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FriendshipService {

    FriendshipRepository friendshipRepository;
    UserRepository userRepository;

    public Optional<FriendInfo> findFriendByEmail(final String email) {
        Optional<Friend> friendOptional = userRepository.findFriendByEmail(email);
        return friendOptional.map(FriendInfo::new);
    }

    public Collection<FriendInfo> getUsersFriends(long id) {
        List<Object[]> pairList = friendshipRepository.getUsersFriendsIdsAndStatuses(id);

        List<FriendInfo> result = new ArrayList<>();
        pairList.forEach(objects -> {
            long userId = ((BigInteger) objects[0]).longValue();
            FriendshipStatus friendshipStatus = (FriendshipStatus.fromString((String) objects[1]));
            boolean isFriendRequester = (Boolean) objects[2];
            Optional<Friend> friendOptional = userRepository.findAllById(userId);
            friendOptional.ifPresent(friend -> result.add(new FriendInfo(friend, friendshipStatus, isFriendRequester)));
        });
        return result;
    }

    @Transactional
    public void editFriendship(FriendshipCommandDto dto) {
        switch (dto.getAction()) {
            case REQUEST:
                this.createFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
            case ACCEPT:
                this.approveFriendshipRequest(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
            case BLOCK:
                this.blockUser(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
            case UNBLOCK:
                this.unblockFriendship(dto.getIdInitiator(), dto.getIdAcceptor());
            case CANCEL:
            case REJECT:
            case UNFRIEND:
                this.cancelFriendship(dto.getIdInitiator(), dto.getIdAcceptor());
                break;
        }
    }

    @SneakyThrows
    @Transactional
    public Friendship blockUser(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException());
        Friendship friendship = friendshipOptional.get();

        assert friendship.getFriendshipStatus().equals(FRIENDS) || friendship.getFriendshipStatus().equals(PENDING);

        LocalDateTime now = LocalDateTime.now();
        friendship.setUpdated(now);
        friendship.setFriendshipStatus(friendship.getRequesterId().equals(actionInitiatorId)
                ? FriendshipStatus.FST_BLOCKED_SND
                : FriendshipStatus.SND_BLOCKED_FST
        );
        friendshipRepository.save(friendship);
        return friendship;
    }

    @SneakyThrows
    @Transactional
    public Friendship cancelFriendship(Long actionInitiatorId, Long actionAcceptorId) {
        assert (!actionInitiatorId.equals(actionAcceptorId));
        friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId).orElseThrow(()
                -> new FriendshipNotFoundException());

        friendshipRepository.deleteFriendshipByIds(actionInitiatorId, actionAcceptorId);
        return null;
    }

    @SneakyThrows
    private void unblockFriendship(Long requesterId, Long requesteeId) {
        this.cancelFriendship(requesterId, requesteeId);
    }

    @SneakyThrows
    private Friendship createFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);
        User recipient = userRepository.getById(actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        if (friendshipOptional.isPresent()) {
            Friendship existingFriendship = friendshipOptional.get();

            if (Long.valueOf(actionAcceptorId).equals(existingFriendship.whoDeniesFriendship())) {
                throw new FriendshipNotAllowedException("User limited your ability to send requests!");
            }

            throw new Exception(String.format("Friendship between %s and %s already exists, status: %s",
                    actionInitiatorId, actionAcceptorId, existingFriendship.getFriendshipStatus()));
        }
        if (recipient.isFriendshipRequestsBlocked()) {
            throw new FriendshipNotAllowedException("User doesn't accept friendship requests!");
        }

        Friendship friendship = new Friendship();
        LocalDateTime now = LocalDateTime.now();
        friendship.setFriendshipStatus(PENDING);
        friendship.setCreated(now);
        friendship.setUpdated(now);
        friendship.setRequesterId(userRepository.getById(actionInitiatorId).getId());
        friendship.setRequesteeId(userRepository.getById(actionAcceptorId).getId());
        friendshipRepository.save(friendship);
        return friendship;
    }

    @SneakyThrows
    private Friendship approveFriendshipRequest(long actionInitiatorId, long actionAcceptorId) {
        assert (actionInitiatorId != actionAcceptorId);

        Optional<Friendship> friendshipOptional = friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId);
        friendshipOptional.orElseThrow(() -> new FriendshipNotFoundException());

        Friendship friendship = friendshipOptional.get();
        assert friendship.getFriendshipStatus().equals(PENDING);

        LocalDateTime now = LocalDateTime.now();
        friendship.setFriendshipStatus(FriendshipStatus.FRIENDS);
        friendship.setUpdated(now);
        friendshipRepository.save(friendship);
        return friendship;
    }
}

package com.linguarium.friendship.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.exception.FriendshipNotAllowedException;
import com.linguarium.friendship.exception.FriendshipNotFoundException;
import com.linguarium.friendship.model.FriendInfoView;
import com.linguarium.friendship.model.FriendWrapper;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.model.FriendshipAction;
import com.linguarium.friendship.model.FriendshipStatus;
import com.linguarium.friendship.repository.FriendshipRepository;
import com.linguarium.user.model.Profile;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class FriendshipServiceImplTest {
    @InjectMocks
    FriendshipServiceImpl friendshipService;

    @Mock
    FriendshipRepository friendshipRepository;

    @Mock
    UserRepository userRepository;

    @DisplayName("Should set FriendshipStatus to FST_BLOCKED_SND when requester is the action initiator")
    @Test
    void givenRequesterIsActionInitiator_whenBlockUser_thenSetFstBlockedSnd() {
        long requesterId = 1L;
        long requesteeId = 2L;
        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.BLOCK);
        Friendship existingFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.FRIENDS);

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.of(existingFriendship));
        when(friendshipRepository.save(existingFriendship)).thenReturn(existingFriendship);

        Friendship result = friendshipService.manageFriendship(dto);

        assertThat(result.getFriendshipStatus()).isEqualTo(FriendshipStatus.FST_BLOCKED_SND);
    }

    @DisplayName("Should set FriendshipStatus to SND_BLOCKED_FST when requestee is the action initiator")
    @Test
    void givenRequesteeIsActionInitiator_whenBlockUser_thenSetSndBlockedFst() {
        long requesterId = 1L;
        long requesteeId = 2L;
        FriendshipActionDto dto = generateFriendshipActionDto(requesteeId, requesterId, FriendshipAction.BLOCK);
        Friendship existingFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.FRIENDS);

        when(friendshipRepository.getFriendshipByUsersIds(requesteeId, requesterId))
                .thenReturn(Optional.of(existingFriendship));
        when(friendshipRepository.save(existingFriendship)).thenReturn(existingFriendship);

        Friendship result = friendshipService.manageFriendship(dto);

        assertThat(result.getFriendshipStatus()).isEqualTo(FriendshipStatus.SND_BLOCKED_FST);
    }

    @DisplayName("Should throw IllegalArgumentException when friendship status is neither FRIENDS nor PENDING")
    @Test
    void givenInvalidFriendshipStatus_whenBlockUser_thenThrowIllegalArgumentException() {
        long requesterId = 1L;
        long requesteeId = 2L;
        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.BLOCK);
        Friendship existingFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.FST_BLOCKED_SND);

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.of(existingFriendship));

        assertThatThrownBy(() -> friendshipService.manageFriendship(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Friendship status must be FRIENDS or PENDING");
    }

    @DisplayName("Should throw FriendshipNotFoundException when friendship does not exist")
    @Test
    void givenInvalidIds_whenBlockUser_thenThrowFriendshipNotFoundException() {
        long requesterId = 1L;
        long requesteeId = 2L;

        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.CANCEL);

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> friendshipService.manageFriendship(dto))
                .isInstanceOf(FriendshipNotFoundException.class);
    }

    @DisplayName("Should return Optional with FriendInfoDto when email exists")
    @Test
    void givenEmailExists_whenFindFriendByEmail_thenReturnOptionalWithFriendInfoDto() {
        FriendWrapper friendWrapper = mock(FriendWrapper.class);
        when(userRepository.findFriendByEmail("test@email.com")).thenReturn(Optional.of(friendWrapper));

        Optional<FriendshipInfoDto> result = friendshipService.findFriendByEmail("test@email.com");

        assertThat(result).isNotEmpty();
        verify(userRepository).findFriendByEmail("test@email.com");
    }

    @DisplayName("Should return empty Optional when email does not exist")
    @Test
    void givenEmailDoesNotExist_whenFindFriendByEmail_thenReturnEmptyOptional() {
        when(userRepository.findFriendByEmail("test@email.com")).thenReturn(Optional.empty());

        Optional<FriendshipInfoDto> result = friendshipService.findFriendByEmail("test@email.com");

        assertThat(result).isEmpty();
        verify(userRepository).findFriendByEmail("test@email.com");
    }

    @DisplayName("Should return list of FriendInfoDto when user ID exists")
    @Test
    void givenUserIdExists_whenGetFriends_thenReturnListOfFriendInfoDto() {
        long userId = 1L;

        FriendInfoView view1 = new FriendInfoView(userId, FriendshipStatus.FRIENDS.getCode(), true);
        FriendInfoView view2 = new FriendInfoView(userId, FriendshipStatus.PENDING.getCode(), false);
        FriendInfoView view3 = new FriendInfoView(userId, FriendshipStatus.FST_BLOCKED_SND.getCode(), true);
        FriendInfoView view4 = new FriendInfoView(userId, FriendshipStatus.SND_BLOCKED_FST.getCode(), false);
        FriendWrapper friendWrapper1 = mock(FriendWrapper.class);
        FriendWrapper friendWrapper2 = mock(FriendWrapper.class);
        FriendWrapper friendWrapper3 = mock(FriendWrapper.class);
        FriendWrapper friendWrapper4 = mock(FriendWrapper.class);

        when(friendshipRepository.findByUserId(userId)).thenReturn(Arrays.asList(view1, view2, view3, view4));
        when(userRepository.findAllById(anyLong()))
                .thenReturn(Optional.of(friendWrapper1))
                .thenReturn(Optional.of(friendWrapper2))
                .thenReturn(Optional.of(friendWrapper3))
                .thenReturn(Optional.of(friendWrapper4));

        Collection<FriendshipInfoDto> result = friendshipService.getFriends(userId);

        assertThat(result).isNotEmpty();
        verify(friendshipRepository).findByUserId(userId);
        verify(userRepository, times(4)).findAllById(anyLong());
    }

    @DisplayName("Should return empty list when user ID does not exist")
    @Test
    void givenUserIdDoesNotExist_whenGetFriends_thenReturnEmptyList() {
        long userId = 1L;
        when(friendshipRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        Collection<FriendshipInfoDto> result = friendshipService.getFriends(userId);

        assertThat(result).isEmpty();
        verify(friendshipRepository).findByUserId(userId);
    }

    @DisplayName("Should delete friendship when canceling with valid IDs")
    @Test
    void givenValidIds_whenCancelFriendship_thenDeleteFriendship() {
        long requesterId = 1L;
        long requesteeId = 2L;

        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.CANCEL);
        Friendship existingFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.FRIENDS);

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.of(existingFriendship));

        friendshipService.manageFriendship(dto);

        verify(friendshipRepository).delete(existingFriendship);
    }

    @DisplayName("Should delete friendship when unblocking with valid IDs")
    @Test
    void givenValidIds_whenUnblockFriendship_thenDeleteFriendship() {
        long requesterId = 1L;
        long requesteeId = 2L;

        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.CANCEL);
        Friendship existingFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.FRIENDS);

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.of(existingFriendship));

        friendshipService.manageFriendship(dto);

        verify(friendshipRepository).delete(existingFriendship);
    }

    @DisplayName("Should throw FriendshipNotFoundException when canceling friendship with invalid IDs")
    @Test
    void givenInvalidIds_whenCancelFriendship_thenThrowFriendshipNotFoundException() {
        long requesterId = 1L;
        long requesteeId = 2L;

        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.CANCEL);

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> friendshipService.manageFriendship(dto))
                .isInstanceOf(FriendshipNotFoundException.class);
    }

    @Test
    @DisplayName(
            """
            Should throw FriendshipNotAllowedException
            when creating a friendship request
            with a user who has blocked requests
            """)
    void givenUserHasBlockedRequests_whenCreateFriendshipRequest_thenThrowFriendshipNotAllowedException() {
        long requesterId = 1L;
        long requesteeId = 2L;
        User recipient = User.builder()
                .profile(Profile.builder().friendshipRequestsBlocked(true).build())
                .build();
        when(userRepository.findById(requesteeId)).thenReturn(Optional.of(recipient));

        FriendshipActionDto dto = FriendshipActionDto.builder()
                .idInitiator(requesterId)
                .idAcceptor(requesteeId)
                .action(FriendshipAction.REQUEST)
                .build();

        assertThatThrownBy(() -> friendshipService.manageFriendship(dto))
                .isInstanceOf(FriendshipNotAllowedException.class);
    }

    @DisplayName("Should throw IllegalArgumentException when approving a friendship request with a non-PENDING status")
    @Test
    void givenNonPendingStatus_whenApproveFriendshipRequest_thenThrowIllegalArgumentException() {
        long requesterId = 1L;
        long requesteeId = 2L;

        Friendship existingFriendship = Friendship.builder()
                .requesterId(requesterId)
                .requesteeId(requesteeId)
                .friendshipStatus(FriendshipStatus.FRIENDS)
                .build();

        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.of(existingFriendship));

        FriendshipActionDto dto = FriendshipActionDto.builder()
                .idInitiator(requesterId)
                .idAcceptor(requesteeId)
                .action(FriendshipAction.ACCEPT)
                .build();

        assertThatThrownBy(() -> friendshipService.manageFriendship(dto)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Should create a new friendship with PENDING status when creating a friendship request")
    @Test
    void givenValidIds_whenCreateFriendshipRequest_thenCreateNewFriendship() {
        long requesterId = 1L;
        long requesteeId = 2L;
        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.REQUEST);

        User mockUser = User.builder().profile(Profile.builder().build()).build();
        when(userRepository.findById(requesteeId)).thenReturn(Optional.of(mockUser));

        Friendship expectedFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.PENDING);
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(expectedFriendship);

        Friendship result = friendshipService.manageFriendship(dto);

        assertThat(result.getFriendshipStatus()).isEqualTo(FriendshipStatus.PENDING);
    }

    @DisplayName("Should update friendship to FRIENDS status when approving a friendship request")
    @Test
    void givenPendingStatus_whenApproveFriendshipRequest_thenUpdateToFriends() {
        long requesterId = 1L;
        long requesteeId = 2L;
        FriendshipActionDto dto = generateFriendshipActionDto(requesterId, requesteeId, FriendshipAction.ACCEPT);

        Friendship existingFriendship = generateFriendship(requesterId, requesteeId, FriendshipStatus.PENDING);
        when(friendshipRepository.getFriendshipByUsersIds(requesterId, requesteeId))
                .thenReturn(Optional.of(existingFriendship));
        when(friendshipRepository.save(existingFriendship)).thenReturn(existingFriendship);

        Friendship result = friendshipService.manageFriendship(dto);

        assertThat(result.getFriendshipStatus()).isEqualTo(FriendshipStatus.FRIENDS);
    }

    @DisplayName("Should throw FriendshipNotAllowedException when friendship already exists")
    @Test
    void givenExistingFriendship_whenCreateFriendshipRequest_thenThrowFriendshipNotAllowedException() {
        long actionInitiatorId = 1L;
        long actionAcceptorId = 2L;
        FriendshipActionDto dto =
                generateFriendshipActionDto(actionInitiatorId, actionAcceptorId, FriendshipAction.REQUEST);
        Friendship existingFriendship =
                generateFriendship(actionInitiatorId, actionAcceptorId, FriendshipStatus.FRIENDS);

        when(friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId))
                .thenReturn(Optional.of(existingFriendship));
        when(userRepository.findById(actionAcceptorId))
                .thenReturn(Optional.of(User.builder().id(actionAcceptorId).build()));

        FriendshipNotAllowedException exception = catchThrowableOfType(
                () -> friendshipService.manageFriendship(dto), FriendshipNotAllowedException.class);

        assertThat(String.format(
                        "Friendship between %s and %s already exists, status: %s",
                        actionInitiatorId, actionAcceptorId, existingFriendship.getFriendshipStatus()))
                .isEqualTo(exception.getMessage());
    }

    @DisplayName("Should throw FriendshipNotAllowedException when userInfo limits ability to send requests")
    @Test
    void givenUserLimitsAbilityToSendRequests_whenCreateFriendshipRequest_thenThrowFriendshipNotAllowedException() {
        long actionInitiatorId = 1L;
        long actionAcceptorId = 2L;
        FriendshipActionDto dto =
                generateFriendshipActionDto(actionInitiatorId, actionAcceptorId, FriendshipAction.REQUEST);

        Friendship existingFriendship = mock(Friendship.class);
        when(existingFriendship.whoDeniesFriendship()).thenReturn(actionAcceptorId);
        when(userRepository.findById(actionAcceptorId))
                .thenReturn(Optional.of(User.builder().id(actionAcceptorId).build()));
        when(friendshipRepository.getFriendshipByUsersIds(actionInitiatorId, actionAcceptorId))
                .thenReturn(Optional.of(existingFriendship));

        assertThatThrownBy(() -> friendshipService.manageFriendship(dto))
                .isInstanceOf(FriendshipNotAllowedException.class);
    }

    private FriendshipActionDto generateFriendshipActionDto(
            long requesterId, long requesteeId, FriendshipAction action) {
        return FriendshipActionDto.builder()
                .idInitiator(requesterId)
                .idAcceptor(requesteeId)
                .action(action)
                .build();
    }

    private Friendship generateFriendship(long requesterId, long requesteeId, FriendshipStatus status) {
        return Friendship.builder()
                .requesterId(requesterId)
                .requesteeId(requesteeId)
                .friendshipStatus(status)
                .build();
    }
}

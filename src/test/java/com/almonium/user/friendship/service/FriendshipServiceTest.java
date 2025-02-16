package com.almonium.user.friendship.service;

import static com.almonium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static com.almonium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.UserService;
import com.almonium.user.friendship.dto.request.FriendshipRequestDto;
import com.almonium.user.friendship.dto.response.PublicUserProfile;
import com.almonium.user.friendship.dto.response.RelatedUserProfile;
import com.almonium.user.friendship.exception.FriendshipException;
import com.almonium.user.friendship.model.entity.Friendship;
import com.almonium.user.friendship.model.enums.FriendshipAction;
import com.almonium.user.friendship.repository.FriendshipRepository;
import com.almonium.util.TestDataGenerator;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class FriendshipServiceTest {
    private static final String FRIENDSHIP_CANT_BE_ESTABLISHED = "Couldn't create friendship request";
    private static final String FRIENDSHIP_IS_ALREADY_BLOCKED = "Friendship is already blocked";

    private static final long REQUESTER_ID = 1L;
    private static final long RECIPIENT_ID = 2L;
    private static final long FRIENDSHIP_ID = 1L;

    @Mock
    FriendshipRepository friendshipRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @InjectMocks
    FriendshipService friendshipService;

    User requester;
    User recipient;
    Friendship friendship;

    @BeforeEach
    void setUp() {
        requester = TestDataGenerator.buildTestUserWithId(REQUESTER_ID);
        recipient = TestDataGenerator.buildTestUserWithId(RECIPIENT_ID);
        friendship = new Friendship(FRIENDSHIP_ID, requester, recipient, Instant.now(), Instant.now(), PENDING);
    }

    @DisplayName("Should return empty list when no friends match username substring")
    @Test
    void givenNonMatchingUsernameSubstring_whenFindFriendsByUsername_thenReturnEmptyList() {
        // Arrange
        String usernameSubstring = "nonexistent";
        long currentUserId = 3L; // ID of the current user to exclude

        when(friendshipRepository.findNewFriendCandidates(currentUserId, usernameSubstring))
                .thenReturn(List.of());

        // Act
        List<PublicUserProfile> result = friendshipService.findUsersByUsername(currentUserId, usernameSubstring);

        // Assert
        assertThat(result).isEmpty();
    }

    @DisplayName("Should return empty list when no friends found for a user")
    @Test
    void givenUserId_whenGetFriends_thenReturnEmptyList() {
        // Act
        List<RelatedUserProfile> result = friendshipService.getFriends(REQUESTER_ID);

        // Assert
        assertThat(result).isEmpty();
    }

    @DisplayName("Should create a new friendship request")
    @Test
    void givenValidFriendshipRequestDto_whenCreateFriendshipRequest_thenFriendshipIsCreated() {
        // Arrange
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);

        when(friendshipRepository.getFriendshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.empty());
        when(userService.getById(RECIPIENT_ID)).thenReturn(recipient);
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship createdFriendship = friendshipService.createFriendshipRequest(requester, dto);

        // Assert
        assertThat(createdFriendship).isNotNull();
        assertThat(createdFriendship.getRequester()).isEqualTo(requester);
        assertThat(createdFriendship.getRequestee()).isEqualTo(recipient);
        assertThat(createdFriendship.getStatus()).isEqualTo(PENDING);
    }

    @DisplayName("Should not create a friendship request when friendship already exists")
    @Test
    void givenExistingFriendship_whenCreateFriendshipRequest_thenThrowException() {
        // Arrange
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);

        when(friendshipRepository.getFriendshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.createFriendshipRequest(requester, dto))
                .isInstanceOf(FriendshipException.class)
                .hasMessage(FRIENDSHIP_CANT_BE_ESTABLISHED);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should not create a friendship request when recipient blocks requests")
    @Test
    void givenRecipientBlocksRequests_whenCreateFriendshipRequest_thenThrowException() {
        // Arrange
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);
        recipient.getProfile().setHidden(true);

        when(friendshipRepository.getFriendshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.empty());
        when(userService.getById(RECIPIENT_ID)).thenReturn(recipient);

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.createFriendshipRequest(requester, dto))
                .isInstanceOf(FriendshipException.class)
                .hasMessage(FRIENDSHIP_CANT_BE_ESTABLISHED);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should accept a pending friendship request")
    @Test
    void givenPendingFriendship_whenAcceptFriendshipRequest_thenStatusIsUpdatedToFriends() {
        // Arrange
        friendship.setStatus(PENDING);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship updatedFriendship =
                friendshipService.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.ACCEPT);

        // Assert
        assertThat(updatedFriendship).isNotNull();
        assertThat(updatedFriendship.getStatus()).isEqualTo(FRIENDS);
    }

    @DisplayName("Should not accept a non-pending friendship request")
    @Test
    void givenNonPendingFriendship_whenAcceptFriendshipRequest_thenThrowException() {
        // Arrange
        friendship.setStatus(FRIENDS);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.ACCEPT))
                .isInstanceOf(FriendshipException.class);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should cancel own friendship request")
    @Test
    void givenPendingFriendship_whenCancelOwnRequest_thenFriendshipIsDeleted() {
        // Arrange
        friendship.setStatus(PENDING);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act
        Friendship deletedFriendship =
                friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.CANCEL);

        // Assert
        assertThat(deletedFriendship).isNotNull();
        verify(friendshipRepository).delete(any(Friendship.class));
    }

    @DisplayName("Should not cancel a non-pending friendship request")
    @Test
    void givenNonPendingFriendship_whenCancelOwnRequest_thenThrowException() {
        // Arrange
        friendship.setStatus(FRIENDS);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.CANCEL))
                .isInstanceOf(FriendshipException.class);

        verify(friendshipRepository, never()).delete(any(Friendship.class));
    }

    @DisplayName("Should reject incoming friendship request")
    @Test
    void givenPendingFriendship_whenRejectIncomingRequest_thenFriendshipIsDeleted() {
        // Arrange
        friendship.setStatus(PENDING);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act
        Friendship deletedFriendship =
                friendshipService.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.REJECT);

        // Assert
        assertThat(deletedFriendship).isNotNull();
        verify(friendshipRepository).delete(any(Friendship.class));
    }

    @DisplayName("Should unfriend a friend")
    @Test
    void givenFriends_whenUnfriend_thenFriendshipIsDeleted() {
        // Arrange
        friendship.setStatus(FRIENDS);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act
        Friendship deletedFriendship =
                friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNFRIEND);

        // Assert
        assertThat(deletedFriendship).isNotNull();
        verify(friendshipRepository).delete(any(Friendship.class));
    }

    @DisplayName("Should block a friend")
    @Test
    void givenFriends_whenBlockFriend_thenStatusIsUpdatedToBlocked() {
        // Arrange
        friendship.setStatus(FRIENDS);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship blockedFriendship =
                friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.BLOCK);

        // Assert
        assertThat(blockedFriendship).isNotNull();
        assertThat(blockedFriendship.getStatus()).isEqualTo(FST_BLOCKED_SND);
    }

    @DisplayName("Should unblock a friend")
    @Test
    void givenBlockedFriend_whenUnblock_thenFriendshipIsDeleted() {
        // Arrange
        friendship.setStatus(FST_BLOCKED_SND);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act
        Friendship unblockedFriendship =
                friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNBLOCK);

        // Assert
        assertThat(unblockedFriendship).isEqualTo(friendship);
        verify(friendshipRepository).delete(friendship);
    }

    @DisplayName("Should throw exception when friendship is not blocked")
    @Test
    void givenNotBlockedFriendship_whenUnblock_thenThrowException() {
        // Arrange
        friendship.setStatus(FRIENDS);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNBLOCK))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("Friendship is not blocked");

        verify(friendshipRepository, never()).delete(any(Friendship.class));
    }

    @DisplayName("Should throw exception when user is not the denier of the friendship")
    @Test
    void givenBlockedFriendship_whenNotDenierUnblock_thenThrowException() {
        // Arrange
        friendship.setStatus(FST_BLOCKED_SND);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.UNBLOCK))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("User is not the denier of this friendship");

        verify(friendshipRepository, never()).delete(any(Friendship.class));
    }

    @DisplayName("Should not block a friendship already blocked by the user")
    @Test
    void givenFriendshipAlreadyBlockedByUser_whenBlockFriend_thenThrowException() {
        // Arrange
        friendship.setStatus(FST_BLOCKED_SND);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.BLOCK))
                .isInstanceOf(FriendshipException.class)
                .hasMessage(FRIENDSHIP_IS_ALREADY_BLOCKED);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should block a friend by requestee")
    @Test
    void givenFriends_whenBlockAsRequestee_thenUpdateStatus() {
        // Arrange
        friendship.setStatus(FRIENDS);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship blockedFriendship =
                friendshipService.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.BLOCK);

        // Assert
        assertThat(blockedFriendship).isNotNull();
        assertThat(blockedFriendship.getStatus()).isEqualTo(SND_BLOCKED_FST);
    }

    @DisplayName("Should throw exception when user is not part of friendship")
    @Test
    void givenInvalidUser_whenManageFriendship_thenThrowException() {
        // Arrange
        User invalidUser = TestDataGenerator.buildTestUserWithId(999L);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(
                        () -> friendshipService.manageFriendship(invalidUser, FRIENDSHIP_ID, FriendshipAction.ACCEPT))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("User is not part of this friendship");

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should throw exception when user is not the recipient of the friendship")
    @Test
    void givenNotRequester_whenValidateCorrectRole_thenThrowException() {
        // Arrange
        friendship.setStatus(PENDING);
        friendship.setRequester(recipient);
        friendship.setRequestee(requester);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(
                        friendship.getRequestee(), FRIENDSHIP_ID, FriendshipAction.CANCEL))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("User is not the requester of this friendship");

        verify(friendshipRepository, never()).delete(any(Friendship.class));
    }

    @DisplayName("Should throw exception when user is not the requestee of the friendship")
    @Test
    void givenNotRequestee_whenValidateCorrectRole_thenThrowException() {
        // Arrange
        friendship.setStatus(PENDING);
        friendship.setRequester(recipient);
        friendship.setRequestee(requester);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() -> friendshipService.manageFriendship(
                        friendship.getRequester(), FRIENDSHIP_ID, FriendshipAction.ACCEPT))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("User is not the requestee of this friendship");

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }
}

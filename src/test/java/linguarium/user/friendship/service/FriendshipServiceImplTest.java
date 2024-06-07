package linguarium.user.friendship.service;

import static linguarium.user.friendship.model.enums.FriendshipStatus.FRIENDS;
import static linguarium.user.friendship.model.enums.FriendshipStatus.FST_BLOCKED_SND;
import static linguarium.user.friendship.model.enums.FriendshipStatus.MUTUALLY_BLOCKED;
import static linguarium.user.friendship.model.enums.FriendshipStatus.PENDING;
import static linguarium.user.friendship.model.enums.FriendshipStatus.SND_BLOCKED_FST;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.repository.UserRepository;
import linguarium.user.core.service.UserService;
import linguarium.user.friendship.dto.FriendDto;
import linguarium.user.friendship.dto.FriendshipRequestDto;
import linguarium.user.friendship.exception.FriendshipNotAllowedException;
import linguarium.user.friendship.model.entity.Friendship;
import linguarium.user.friendship.model.enums.FriendStatus;
import linguarium.user.friendship.model.enums.FriendshipAction;
import linguarium.user.friendship.model.projection.FriendshipToUserProjection;
import linguarium.user.friendship.model.projection.UserToFriendProjection;
import linguarium.user.friendship.repository.FriendshipRepository;
import linguarium.user.friendship.service.impl.FriendshipServiceImpl;
import linguarium.util.TestDataGenerator;
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
class FriendshipServiceImplTest {
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
    FriendshipServiceImpl friendshipServiceImpl;

    User requester;
    User recipient;
    Friendship friendship;

    @BeforeEach
    void setUp() {
        requester = TestDataGenerator.buildTestUserWithId(REQUESTER_ID);
        recipient = TestDataGenerator.buildTestUserWithId(RECIPIENT_ID);
        friendship =
                new Friendship(FRIENDSHIP_ID, requester, recipient, LocalDateTime.now(), LocalDateTime.now(), PENDING);
    }

    @DisplayName("Should find friend by email")
    @Test
    void givenEmail_whenFindFriendByEmail_thenReturnFriendDto() {
        // Arrange
        String email = "test@example.com";
        String username = "username";
        UserToFriendProjection projection =
                TestDataGenerator.buildTestUserToFriendProjection(REQUESTER_ID, username, email);
        when(userRepository.findFriendByEmail(email)).thenReturn(Optional.of(projection));

        // Act
        Optional<FriendDto> result = friendshipServiceImpl.findFriendByEmail(email);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(REQUESTER_ID);
        assertThat(result.get().getEmail()).isEqualTo(email);
        assertThat(result.get().getUsername()).isEqualTo(username);
    }

    @DisplayName("Should return empty when friend not found by email")
    @Test
    void givenNonExistingEmail_whenFindFriendByEmail_thenReturnEmpty() {
        // Arrange
        String email = "nonexistent@example.com";
        when(userRepository.findFriendByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<FriendDto> result = friendshipServiceImpl.findFriendByEmail(email);

        // Assert
        assertThat(result).isEmpty();
    }

    @DisplayName("Should return friends for a user")
    @Test
    void givenUserId_whenGetFriends_thenReturnListOfFriends() {
        // Arrange
        FriendshipToUserProjection projection = new FriendshipToUserProjection(RECIPIENT_ID, FRIENDS, true);
        String username = "friendUsername";
        String email = "friend@example.com";
        UserToFriendProjection friendProjection =
                TestDataGenerator.buildTestUserToFriendProjection(RECIPIENT_ID, username, email);

        when(friendshipRepository.getVisibleFriendships(REQUESTER_ID)).thenReturn(List.of(projection));
        when(userRepository.findUserById(RECIPIENT_ID)).thenReturn(Optional.of(friendProjection));

        // Act
        List<FriendDto> result = friendshipServiceImpl.getFriends(REQUESTER_ID);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(RECIPIENT_ID);
        assertThat(result.get(0).getUsername()).isEqualTo(username);
        assertThat(result.get(0).getEmail()).isEqualTo(email);
        assertThat(result.get(0).getStatus()).isEqualTo(FriendStatus.FRIENDS);
    }

    @DisplayName("Should return empty list when no friends found for a user")
    @Test
    void givenUserId_whenGetFriends_thenReturnEmptyList() {
        // Arrange
        when(friendshipRepository.getVisibleFriendships(REQUESTER_ID)).thenReturn(Collections.emptyList());

        // Act
        List<FriendDto> result = friendshipServiceImpl.getFriends(REQUESTER_ID);

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
        Friendship createdFriendship = friendshipServiceImpl.createFriendshipRequest(requester, dto);

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
        assertThatThrownBy(() -> friendshipServiceImpl.createFriendshipRequest(requester, dto))
                .isInstanceOf(FriendshipNotAllowedException.class)
                .hasMessage(FRIENDSHIP_CANT_BE_ESTABLISHED);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should not create a friendship request when recipient blocks requests")
    @Test
    void givenRecipientBlocksRequests_whenCreateFriendshipRequest_thenThrowException() {
        // Arrange
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);
        recipient.getProfile().setFriendshipRequestsBlocked(true);

        when(friendshipRepository.getFriendshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.empty());
        when(userService.getById(RECIPIENT_ID)).thenReturn(recipient);

        // Act & Assert
        assertThatThrownBy(() -> friendshipServiceImpl.createFriendshipRequest(requester, dto))
                .isInstanceOf(FriendshipNotAllowedException.class)
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
                friendshipServiceImpl.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.ACCEPT);

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
        assertThatThrownBy(
                        () -> friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.ACCEPT))
                .isInstanceOf(FriendshipNotAllowedException.class);

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
                friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.CANCEL);

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
        assertThatThrownBy(
                        () -> friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.CANCEL))
                .isInstanceOf(FriendshipNotAllowedException.class);

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
                friendshipServiceImpl.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.REJECT);

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
                friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNFRIEND);

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
                friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.BLOCK);

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
                friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNBLOCK);

        // Assert
        assertThat(unblockedFriendship).isEqualTo(friendship);
        verify(friendshipRepository).delete(friendship);
    }

    @DisplayName("Should unblock a mutually blocked friendship")
    @Test
    void givenMutuallyBlockedFriendship_whenUnblock_thenStatusIsUpdated() {
        // Arrange
        friendship.setStatus(MUTUALLY_BLOCKED);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship unblockedFriendship =
                friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNBLOCK);

        // Assert
        assertThat(unblockedFriendship).isNotNull();
        assertThat(unblockedFriendship.getStatus()).isEqualTo(SND_BLOCKED_FST);
        verify(friendshipRepository).save(friendship);
    }

    @DisplayName("Should unblock a mutually blocked friendship as requestee")
    @Test
    void givenMutuallyBlockedFriendshipAsRequestee_whenUnblock_thenStatusIsUpdated() {
        // Arrange
        friendship.setStatus(MUTUALLY_BLOCKED);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship unblockedFriendship =
                friendshipServiceImpl.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.UNBLOCK);

        // Assert
        assertThat(unblockedFriendship).isNotNull();
        assertThat(unblockedFriendship.getStatus()).isEqualTo(FST_BLOCKED_SND);
        verify(friendshipRepository).save(friendship);
    }

    @DisplayName("Should throw exception when friendship is not blocked")
    @Test
    void givenNotBlockedFriendship_whenUnblock_thenThrowException() {
        // Arrange
        friendship.setStatus(FRIENDS);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(() ->
                        friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.UNBLOCK))
                .isInstanceOf(FriendshipNotAllowedException.class)
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
        assertThatThrownBy(() ->
                        friendshipServiceImpl.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.UNBLOCK))
                .isInstanceOf(FriendshipNotAllowedException.class)
                .hasMessage("User is not the denier of this friendship");

        verify(friendshipRepository, never()).delete(any(Friendship.class));
    }

    @DisplayName("Should not block a mutually blocked friendship")
    @Test
    void givenMutuallyBlockedFriendship_whenBlockFriend_thenThrowException() {
        // Arrange
        friendship.setStatus(MUTUALLY_BLOCKED);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(
                        () -> friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.BLOCK))
                .isInstanceOf(FriendshipNotAllowedException.class)
                .hasMessage(FRIENDSHIP_IS_ALREADY_BLOCKED);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should not block a friendship already blocked by the user")
    @Test
    void givenFriendshipAlreadyBlockedByUser_whenBlockFriend_thenThrowException() {
        // Arrange
        friendship.setStatus(FST_BLOCKED_SND);
        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));

        // Act & Assert
        assertThatThrownBy(
                        () -> friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.BLOCK))
                .isInstanceOf(FriendshipNotAllowedException.class)
                .hasMessage(FRIENDSHIP_IS_ALREADY_BLOCKED);

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }

    @DisplayName("Should set friendship status to MUTUALLY_BLOCKED when blocked by the other user")
    @Test
    void givenFriendshipBlockedByOtherUser_whenBlockFriend_thenStatusIsUpdatedToMutuallyBlocked() {
        // Arrange
        friendship.setStatus(SND_BLOCKED_FST);

        when(friendshipRepository.findById(FRIENDSHIP_ID)).thenReturn(Optional.of(friendship));
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        // Act
        Friendship blockedFriendship =
                friendshipServiceImpl.manageFriendship(requester, FRIENDSHIP_ID, FriendshipAction.BLOCK);

        // Assert
        assertThat(blockedFriendship).isNotNull();
        assertThat(blockedFriendship.getStatus()).isEqualTo(MUTUALLY_BLOCKED);
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
                friendshipServiceImpl.manageFriendship(recipient, FRIENDSHIP_ID, FriendshipAction.BLOCK);

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
        assertThatThrownBy(() ->
                        friendshipServiceImpl.manageFriendship(invalidUser, FRIENDSHIP_ID, FriendshipAction.ACCEPT))
                .isInstanceOf(FriendshipNotAllowedException.class)
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
        assertThatThrownBy(() -> friendshipServiceImpl.manageFriendship(
                        friendship.getRequestee(), FRIENDSHIP_ID, FriendshipAction.CANCEL))
                .isInstanceOf(FriendshipNotAllowedException.class)
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
        assertThatThrownBy(() -> friendshipServiceImpl.manageFriendship(
                        friendship.getRequester(), FRIENDSHIP_ID, FriendshipAction.ACCEPT))
                .isInstanceOf(FriendshipNotAllowedException.class)
                .hasMessage("User is not the requestee of this friendship");

        verify(friendshipRepository, never()).save(any(Friendship.class));
    }
}

package com.almonium.user.relationship.service;

import static com.almonium.user.relationship.model.enums.RelationshipStatus.CANCELLED;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FRIENDS;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.PENDING;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.REJECTED;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.SND_BLOCKED_FST;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.UNFRIENDED;
import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.infra.notification.service.NotificationService;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import com.almonium.user.core.service.ProfileService;
import com.almonium.user.core.service.UserService;
import com.almonium.user.relationship.dto.request.FriendshipRequestDto;
import com.almonium.user.relationship.dto.response.RelatedUserProfile;
import com.almonium.user.relationship.exception.RelationshipException;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelationshipAction;
import com.almonium.user.relationship.model.projection.LearnerLanguageProjection;
import com.almonium.user.relationship.repository.RelationshipRepository;
import com.almonium.util.TestDataGenerator;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class RelationshipServiceTest {
    private static final String RELATIONSHIP_CANT_BE_ESTABLISHED = "Couldn't create or re-establish relationship";
    private static final String RELATIONSHIP_IS_ALREADY_BLOCKED = "Relationship is already blocked";

    private static final UUID REQUESTER_ID = UUID.randomUUID();
    private static final UUID RECIPIENT_ID = UUID.randomUUID();
    private static final UUID RELATIONSHIP_ID = UUID.randomUUID();

    @Mock
    RelationshipRepository relationshipRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @Mock
    ProfileService profileService;

    @Mock
    NotificationService notificationService;

    @Mock
    EffectiveAccessService effectiveAccessService;

    @Spy
    RelationshipStateMachine stateMachine = new RelationshipStateMachine();

    @InjectMocks
    RelationshipService relationshipService;

    User requester;
    User recipient;
    Relationship relationship;

    @BeforeEach
    void setUp() {
        requester = TestDataGenerator.buildTestUserWithId(REQUESTER_ID);
        recipient = TestDataGenerator.buildTestUserWithId(RECIPIENT_ID);
        relationship =
                new Relationship(RELATIONSHIP_ID, requester, recipient, Instant.now(), Instant.now(), PENDING, 0);
    }

    @DisplayName("Should return empty list when no account matches the username substring")
    @Test
    void givenNonMatchingUsernameSubstring_whenFindFriendsByUsername_thenReturnEmptyList() {
        // Arrange
        String usernameSubstring = "nonexistent";
        var currentUserId = UUID.randomUUID(); // ID of the current user to exclude

        when(relationshipRepository.searchUsersByUsername(currentUserId, usernameSubstring))
                .thenReturn(List.of());

        // Act
        List<RelatedUserProfile> result = relationshipService.findUsersByUsername(currentUserId, usernameSubstring);

        // Assert
        assertThat(result).isEmpty();
    }

    @DisplayName("Should stamp membership from the access service, not from what the query returned")
    @Test
    void givenGrantedMember_whenGetFriends_thenTheAccessServiceDecidesWhoIsAMember() {
        UUID memberId = UUID.randomUUID();
        UUID freeId = UUID.randomUUID();
        RelatedUserProfile member = new RelatedUserProfile(memberId, "member", null, RELATIONSHIP_ID, "FRIENDS");
        RelatedUserProfile free = new RelatedUserProfile(freeId, "free", null, RELATIONSHIP_ID, "FRIENDS");

        when(relationshipRepository.getFriendships(REQUESTER_ID)).thenReturn(List.of(member, free));
        when(effectiveAccessService.premiumAmong(List.of(memberId, freeId))).thenReturn(Set.of(memberId));

        List<RelatedUserProfile> friends = relationshipService.getFriends(REQUESTER_ID);

        assertThat(friends)
                .extracting(RelatedUserProfile::getUsername, RelatedUserProfile::isPremium)
                .containsExactly(tuple("member", true), tuple("free", false));
    }

    @DisplayName("Should stamp each person with the languages they study, and leave the rest empty")
    @Test
    void givenLearners_whenGetFriends_thenEachRowCarriesItsOwnLanguages() {
        UUID studentId = UUID.randomUUID();
        UUID quietId = UUID.randomUUID();
        RelatedUserProfile student = new RelatedUserProfile(studentId, "student", null, RELATIONSHIP_ID, "FRIENDS");
        RelatedUserProfile quiet = new RelatedUserProfile(quietId, "quiet", null, RELATIONSHIP_ID, "FRIENDS");

        when(relationshipRepository.getFriendships(REQUESTER_ID)).thenReturn(List.of(student, quiet));
        when(relationshipRepository.findActiveLanguagesOf(List.of(studentId, quietId)))
                .thenReturn(List.of(
                        new LearnerLanguageProjection(studentId, Language.ES),
                        new LearnerLanguageProjection(studentId, Language.DE)));

        List<RelatedUserProfile> friends = relationshipService.getFriends(REQUESTER_ID);

        assertThat(friends)
                .extracting(RelatedUserProfile::getUsername, RelatedUserProfile::getLearning)
                .containsExactly(tuple("student", List.of(Language.ES, Language.DE)), tuple("quiet", List.of()));
    }

    @DisplayName("Should return empty list when no friends found for a user")
    @Test
    void givenUserId_whenGetFriends_thenReturnEmptyList() {
        // Act
        List<RelatedUserProfile> result = relationshipService.getFriends(REQUESTER_ID);

        // Assert
        assertThat(result).isEmpty();
    }

    @DisplayName("Should create a new relationship request")
    @Test
    void givenValidRelationshipRequestDto_whenCreateFriendshipRequest_thenFriendshipIsCreated() {
        // ─── Arrange ──────────────────────────────────────────────────────────────
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);

        when(relationshipRepository.getRelationshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.empty());

        // stub the profile lookup used inside createFriendshipAndNotify(...)
        when(profileService.getProfileById(RECIPIENT_ID)).thenReturn(recipient.getProfile());

        when(relationshipRepository.save(any(Relationship.class)))
                .thenAnswer(inv -> inv.getArgument(0)); // return the object just saved

        // ─── Act ──────────────────────────────────────────────────────────────────
        Relationship createdRelationship = relationshipService.createFriendshipRequest(requester, dto);

        // ─── Assert ───────────────────────────────────────────────────────────────
        assertThat(createdRelationship).isNotNull();
        assertThat(createdRelationship.getRequester()).isEqualTo(requester);
        assertThat(createdRelationship.getRequestee()).isEqualTo(recipient);
        assertThat(createdRelationship.getStatus()).isEqualTo(PENDING);
    }

    @DisplayName("Should not create a friendship request when friendship already exists")
    @Test
    void givenExistingFriendship_whenCreateFriendshipRequest_thenThrowException() {
        // Arrange
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);

        when(relationshipRepository.getRelationshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() -> relationshipService.createFriendshipRequest(requester, dto))
                .isInstanceOf(RelationshipException.class)
                .hasMessage(RELATIONSHIP_CANT_BE_ESTABLISHED);

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should not create a friendship request when recipient blocks requests")
    @Test
    void givenRecipientBlocksRequests_whenCreateFriendshipRequest_thenThrowException() {
        // Arrange
        FriendshipRequestDto dto = new FriendshipRequestDto(RECIPIENT_ID);

        // mark profile as hidden → blocks friend-requests
        recipient.getProfile().setHidden(true);

        when(relationshipRepository.getRelationshipByUsersIds(REQUESTER_ID, RECIPIENT_ID))
                .thenReturn(Optional.empty());

        // stub the *profile* lookup used by createFriendshipAndNotify(...)
        when(profileService.getProfileById(RECIPIENT_ID)).thenReturn(recipient.getProfile());

        // Act & Assert
        assertThatThrownBy(() -> relationshipService.createFriendshipRequest(requester, dto))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("Couldn't create or re-establish relationship");

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should not create a relationship with oneself")
    @Test
    void givenSelfAsRecipient_whenCreateFriendshipRequest_thenThrowException() {
        FriendshipRequestDto dto = new FriendshipRequestDto(REQUESTER_ID);

        assertThatThrownBy(() -> relationshipService.createFriendshipRequest(requester, dto))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("A user cannot have a relationship with themselves");

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should recheck recipient privacy when re-establishing a relationship")
    @Test
    void givenHiddenRecipientAndRetryableRelationship_whenReestablishing_thenThrowException() {
        relationship.setStatus(UNFRIENDED);
        requester.getProfile().setHidden(true);
        FriendshipRequestDto dto = new FriendshipRequestDto(REQUESTER_ID);

        when(relationshipRepository.getRelationshipByUsersIds(RECIPIENT_ID, REQUESTER_ID))
                .thenReturn(Optional.of(relationship));
        when(profileService.getProfileById(REQUESTER_ID)).thenReturn(requester.getProfile());

        assertThatThrownBy(() -> relationshipService.createFriendshipRequest(recipient, dto))
                .isInstanceOf(RelationshipException.class)
                .hasMessage(RELATIONSHIP_CANT_BE_ESTABLISHED);

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should not block oneself")
    @Test
    void givenSelfAsTarget_whenBlockingUser_thenThrowException() {
        assertThatThrownBy(() -> relationshipService.blockUser(requester, REQUESTER_ID))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("A user cannot have a relationship with themselves");

        verify(relationshipRepository, never()).getRelationshipByUsersIds(REQUESTER_ID, REQUESTER_ID);
    }

    @DisplayName("Should accept a pending friendship request")
    @Test
    void givenPendingFriendship_whenAcceptFriendshipRequest_thenStatusIsUpdatedToFriends() {
        // Arrange
        relationship.setStatus(PENDING);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship updatedRelationship =
                relationshipService.manageFriendship(recipient, RELATIONSHIP_ID, RelationshipAction.ACCEPT);

        // Assert
        assertThat(updatedRelationship).isNotNull();
        assertThat(updatedRelationship.getStatus()).isEqualTo(FRIENDS);
    }

    @DisplayName("Should not accept a non-pending friendship request")
    @Test
    void givenNonPendingFriendship_whenAcceptFriendshipRequest_thenThrowException() {
        // Arrange
        relationship.setStatus(FRIENDS);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() ->
                        relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.ACCEPT))
                .isInstanceOf(RelationshipException.class);

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should cancel own friendship request")
    @Test
    void givenPendingFriendship_whenCancelOwnRequest_thenFriendshipIsDeleted() {
        // Arrange
        relationship.setStatus(PENDING);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship result =
                relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.CANCEL);

        // Assert
        assertThat(result).isNotNull();
        verify(relationshipRepository, never()).delete(any(Relationship.class));
        assertThat(relationship.getStatus()).isEqualTo(CANCELLED);
    }

    @DisplayName("Should not cancel a non-pending friendship request")
    @Test
    void givenNonPendingFriendship_whenCancelOwnRequest_thenThrowException() {
        // Arrange
        relationship.setStatus(FRIENDS);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() ->
                        relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.CANCEL))
                .isInstanceOf(RelationshipException.class);

        verify(relationshipRepository, never()).delete(any(Relationship.class));
    }

    @DisplayName("Should reject incoming friendship request")
    @Test
    void givenPendingFriendship_whenRejectIncomingRequest_thenFriendshipIsDeleted() {
        // Arrange
        relationship.setStatus(PENDING);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship result =
                relationshipService.manageFriendship(recipient, RELATIONSHIP_ID, RelationshipAction.REJECT);

        // Assert
        assertThat(result).isNotNull();
        verify(relationshipRepository, never()).delete(any(Relationship.class));
        assertThat(relationship.getStatus()).isEqualTo(REJECTED);
    }

    @DisplayName("Should unfriend a friend")
    @Test
    void givenFriends_whenUnfriend_thenFriendshipIsDeleted() {
        // Arrange
        relationship.setStatus(FRIENDS);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship deletedRelationship =
                relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.UNFRIEND);

        // Assert
        assertThat(deletedRelationship).isNotNull();
        assertThat(deletedRelationship.getStatus()).isEqualTo(UNFRIENDED);
        verify(relationshipRepository, never()).delete(any(Relationship.class));
    }

    @DisplayName("Should block a friend")
    @Test
    void givenFriends_whenBlockFriend_thenStatusIsUpdatedToBlocked() {
        // Arrange
        relationship.setStatus(FRIENDS);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship blockedRelationship =
                relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.BLOCK);

        // Assert
        assertThat(blockedRelationship).isNotNull();
        assertThat(blockedRelationship.getStatus()).isEqualTo(FST_BLOCKED_SND);
    }

    @DisplayName("Should unblock a friend")
    @Test
    void givenBlockedFriend_whenUnblock_thenFriendshipIsDeleted() {
        // Arrange
        relationship.setStatus(FST_BLOCKED_SND);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship unblockedRelationship =
                relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.UNBLOCK);

        // Assert
        assertThat(unblockedRelationship).isEqualTo(relationship);
    }

    @DisplayName("Should throw exception when friendship is not blocked")
    @Test
    void givenNotBlockedFriendship_whenUnblock_thenThrowException() {
        // Arrange
        relationship.setStatus(FRIENDS);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() ->
                        relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.UNBLOCK))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("Friendship is not blocked");

        verify(relationshipRepository, never()).delete(any(Relationship.class));
    }

    @DisplayName("Should throw exception when user is not the denier of the relationship")
    @Test
    void givenBlockedFriendship_whenNotDenierUnblock_thenThrowException() {
        // Arrange
        relationship.setStatus(FST_BLOCKED_SND);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() ->
                        relationshipService.manageFriendship(recipient, RELATIONSHIP_ID, RelationshipAction.UNBLOCK))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("User is not the denier of this relationship");

        verify(relationshipRepository, never()).delete(any(Relationship.class));
    }

    @DisplayName("Should not block a friendship already blocked by the user")
    @Test
    void givenFriendshipAlreadyBlockedByUser_whenBlockFriend_thenThrowException() {
        // Arrange
        relationship.setStatus(FST_BLOCKED_SND);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() ->
                        relationshipService.manageFriendship(requester, RELATIONSHIP_ID, RelationshipAction.BLOCK))
                .isInstanceOf(RelationshipException.class)
                .hasMessage(RELATIONSHIP_IS_ALREADY_BLOCKED);

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should block a friend by requestee")
    @Test
    void givenFriends_whenBlockAsRequestee_thenUpdateStatus() {
        // Arrange
        relationship.setStatus(FRIENDS);
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));
        when(relationshipRepository.save(any(Relationship.class))).thenReturn(relationship);

        // Act
        Relationship blockedRelationship =
                relationshipService.manageFriendship(recipient, RELATIONSHIP_ID, RelationshipAction.BLOCK);

        // Assert
        assertThat(blockedRelationship).isNotNull();
        assertThat(blockedRelationship.getStatus()).isEqualTo(SND_BLOCKED_FST);
    }

    @DisplayName("Should throw exception when user is not part of friendship")
    @Test
    void givenInvalidUser_whenManageFriendship_thenThrowException() {
        // Arrange
        User invalidUser = TestDataGenerator.buildTestUserWithId(UUID.randomUUID());
        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() ->
                        relationshipService.manageFriendship(invalidUser, RELATIONSHIP_ID, RelationshipAction.ACCEPT))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("User is not part of this relationship");

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }

    @DisplayName("Should throw exception when user is not the recipient of the friendship")
    @Test
    void givenNotRequester_whenValidateCorrectRole_thenThrowException() {
        // Arrange
        relationship.setStatus(PENDING);
        relationship.setRequester(recipient);
        relationship.setRequestee(requester);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() -> relationshipService.manageFriendship(
                        relationship.getRequestee(), RELATIONSHIP_ID, RelationshipAction.CANCEL))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("User is not the requester of this relationship");

        verify(relationshipRepository, never()).delete(any(Relationship.class));
    }

    @DisplayName("Should throw exception when user is not the requestee of the friendship")
    @Test
    void givenNotRequestee_whenValidateCorrectRole_thenThrowException() {
        // Arrange
        relationship.setStatus(PENDING);
        relationship.setRequester(recipient);
        relationship.setRequestee(requester);

        when(relationshipRepository.findById(RELATIONSHIP_ID)).thenReturn(Optional.of(relationship));

        // Act & Assert
        assertThatThrownBy(() -> relationshipService.manageFriendship(
                        relationship.getRequester(), RELATIONSHIP_ID, RelationshipAction.ACCEPT))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("User is not the requestee of this relationship");

        verify(relationshipRepository, never()).save(any(Relationship.class));
    }
}

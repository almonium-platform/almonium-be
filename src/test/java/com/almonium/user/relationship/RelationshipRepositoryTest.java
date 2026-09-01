package com.almonium.user.relationship;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.user.relationship.dto.response.RelatedUserProfile;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import com.almonium.user.relationship.model.projection.LearnerLanguageProjection;
import com.almonium.user.relationship.model.projection.RelationshipToUserProjection;
import com.almonium.user.relationship.repository.RelationshipRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@FieldDefaults(level = PRIVATE)
@Sql(scripts = "classpath:db/add-relationships.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RelationshipRepositoryTest {
    private static final UUID REQUESTER_ID = UUID.fromString("01956cde-a541-7ac1-8b32-2896d096ec01");
    private static final UUID REQUESTEE_ID = UUID.fromString("01956cde-d6dd-7aca-bd07-e5c29cadf093");

    @Autowired
    RelationshipRepository relationshipRepository;

    @DisplayName("Should find friendship by user IDs")
    @Test
    void givenUserIds_whenGetFriendshipByUsersIds_thenRelationshipShouldBePresent() {
        Optional<Relationship> friendship =
                relationshipRepository.getRelationshipByUsersIds(REQUESTER_ID, REQUESTEE_ID);
        assertThat(friendship).isPresent();
    }

    @DisplayName("Should find friend info by user ID")
    @Test
    void givenUserId_whenGetVisibleFriendships_thenFriendInfoViewShouldBePresent() {
        List<RelationshipToUserProjection> relationshipToUserProjections =
                relationshipRepository.getVisibleFriendships(REQUESTER_ID);
        assertThat(relationshipToUserProjections).isNotEmpty();
    }

    @DisplayName("Should leave membership to the service, not re-derive it from plan rows")
    @Test
    void givenPremiumFriend_whenGetFriendships_thenPremiumIsLeftUnset() {
        List<RelatedUserProfile> friends = relationshipRepository.getFriendships(REQUESTER_ID);

        // user2 pays for a premium plan in the fixture, and the query still must not answer this: only
        // EffectiveAccessService knows, because an operator grant is a membership no plan row records.
        assertThat(friends)
                .singleElement()
                .extracting(RelatedUserProfile::isPremium)
                .isEqualTo(false);
    }

    @DisplayName("Should return every matching account, each with how the searcher stands with it")
    @Test
    void givenUsernameSubstring_whenSearchUsersByUsername_thenFriendsAndStrangersAreBothReturned() {
        List<RelatedUserProfile> results = relationshipRepository.searchUsersByUsername(REQUESTER_ID, "user");

        assertThat(results)
                .extracting(RelatedUserProfile::getUsername, RelatedUserProfile::getRelationshipStatus)
                .containsExactlyInAnyOrder(
                        tuple("user2", RelativeRelationshipStatus.FRIENDS),
                        tuple("user3", RelativeRelationshipStatus.STRANGER));

        assertThat(results)
                .filteredOn(profile -> profile.getUsername().equals("user3"))
                .singleElement()
                .extracting(RelatedUserProfile::getRelationshipId)
                .isNull();
    }

    @DisplayName("Should answer with the languages a batch of people are actively studying")
    @Test
    void givenUserIds_whenFindActiveLanguagesOf_thenOnlyActiveLearnersAreReturned() {
        UUID strangerId = UUID.fromString("01956cde-e7c1-7b0a-9f21-3b41f0a5c0d3");

        List<LearnerLanguageProjection> languages =
                relationshipRepository.findActiveLanguagesOf(List.of(REQUESTER_ID, REQUESTEE_ID, strangerId));

        // user3's only language is set aside, so it never reaches a row.
        assertThat(languages)
                .extracting(LearnerLanguageProjection::getUserId, LearnerLanguageProjection::getLanguage)
                .containsExactlyInAnyOrder(tuple(REQUESTEE_ID, Language.DE), tuple(REQUESTEE_ID, Language.ES));
    }

    @DisplayName("Should reject a second relationship with reversed users")
    @Test
    void givenExistingRelationship_whenSavingReversedPair_thenConstraintRejectsIt() {
        Relationship existing = relationshipRepository
                .getRelationshipByUsersIds(REQUESTER_ID, REQUESTEE_ID)
                .orElseThrow();
        Relationship reversed = new Relationship(existing.getRequestee(), existing.getRequester());

        assertThatThrownBy(() -> relationshipRepository.saveAndFlush(reversed))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("Should reject a relationship from a user to themselves")
    @Test
    void givenSameUserAtBothEnds_whenSavingRelationship_thenConstraintRejectsIt() {
        Relationship existing = relationshipRepository
                .getRelationshipByUsersIds(REQUESTER_ID, REQUESTEE_ID)
                .orElseThrow();
        Relationship selfRelationship = new Relationship(existing.getRequester(), existing.getRequester());

        assertThatThrownBy(() -> relationshipRepository.saveAndFlush(selfRelationship))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

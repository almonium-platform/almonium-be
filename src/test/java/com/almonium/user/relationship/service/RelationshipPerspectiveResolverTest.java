package com.almonium.user.relationship.service;

import static com.almonium.user.relationship.model.enums.RelationshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.MUTUAL_BLOCK;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.UNFRIENDED;
import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.user.core.model.entity.User;
import com.almonium.user.relationship.model.entity.Relationship;
import com.almonium.user.relationship.model.enums.RelativeRelationshipStatus;
import com.almonium.user.relationship.model.record.RelationshipPerspective;
import com.almonium.util.TestDataGenerator;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RelationshipPerspectiveResolverTest {
    private final RelationshipPerspectiveResolver resolver = new RelationshipPerspectiveResolver();

    private User requester;
    private User requestee;
    private Relationship relationship;

    @BeforeEach
    void setUp() {
        requester = TestDataGenerator.buildTestUserWithId(UUID.randomUUID());
        requestee = TestDataGenerator.buildTestUserWithId(UUID.randomUUID());
        relationship = new Relationship(requester, requestee);
    }

    @Test
    void pendingRequestIsOutgoingForRequesterAndIncomingForRequestee() {
        RelationshipPerspective requesterView = resolveFor(requester, requestee, false);
        RelationshipPerspective requesteeView = resolveFor(requestee, requester, false);

        assertThat(requesterView.status()).isEqualTo(RelativeRelationshipStatus.PENDING_OUTGOING);
        assertThat(requesterView.counterpart()).isEqualTo(requestee);
        assertThat(requesterView.canAccept()).isFalse();
        assertThat(requesteeView.status()).isEqualTo(RelativeRelationshipStatus.PENDING_INCOMING);
        assertThat(requesteeView.counterpart()).isEqualTo(requester);
        assertThat(requesteeView.canAccept()).isTrue();
    }

    @Test
    void unilateralBlockHasDifferentPrivacyForEachViewer() {
        relationship.setStatus(FST_BLOCKED_SND);

        RelationshipPerspective blockerView = resolveFor(requester, requestee, false);
        RelationshipPerspective blockedView = resolveFor(requestee, requester, false);

        assertThat(blockerView.status()).isEqualTo(RelativeRelationshipStatus.BLOCKED);
        assertThat(blockerView.canUnblock()).isTrue();
        assertThat(blockerView.canBlock()).isFalse();
        assertThat(blockedView.status()).isEqualTo(RelativeRelationshipStatus.STRANGER);
        assertThat(blockedView.profileVisible()).isFalse();
        assertThat(blockedView.acceptsRequests()).isFalse();
        assertThat(blockedView.canBlock()).isTrue();
    }

    @Test
    void mutualBlockIsBlockedAndHiddenForEitherViewer() {
        relationship.setStatus(MUTUAL_BLOCK);

        RelationshipPerspective requesterView = resolveFor(requester, requestee, false);
        RelationshipPerspective requesteeView = resolveFor(requestee, requester, false);

        assertThat(requesterView.status()).isEqualTo(RelativeRelationshipStatus.BLOCKED);
        assertThat(requesteeView.status()).isEqualTo(RelativeRelationshipStatus.BLOCKED);
        assertThat(requesterView.profileVisible()).isFalse();
        assertThat(requesteeView.profileVisible()).isFalse();
        assertThat(requesterView.canUnblock()).isTrue();
        assertThat(requesteeView.canUnblock()).isTrue();
    }

    @Test
    void retryableRelationshipRespectsCurrentProfilePrivacy() {
        relationship.setStatus(UNFRIENDED);

        RelationshipPerspective perspective = resolveFor(requester, requestee, true);

        assertThat(perspective.status()).isEqualTo(RelativeRelationshipStatus.STRANGER);
        assertThat(perspective.profileVisible()).isFalse();
        assertThat(perspective.canRequest()).isFalse();
        assertThat(perspective.acceptsRequests()).isFalse();
    }

    @Test
    void publicStrangerCanReceiveRequests() {
        RelationshipPerspective perspective = resolver.stranger(requestee, false);

        assertThat(perspective.relationship()).isEmpty();
        assertThat(perspective.relationshipId()).isNull();
        assertThat(perspective.counterpart()).isEqualTo(requestee);
        assertThat(perspective.status()).isEqualTo(RelativeRelationshipStatus.STRANGER);
        assertThat(perspective.canRequest()).isTrue();
        assertThat(perspective.canBlock()).isTrue();
        assertThat(perspective.profileVisible()).isTrue();
    }

    private RelationshipPerspective resolveFor(User viewer, User counterpart, boolean profileHidden) {
        return resolver.resolve(viewer.getId(), counterpart, Optional.of(relationship), profileHidden);
    }
}

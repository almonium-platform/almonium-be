package com.almonium.user.relationship.service;

import static com.almonium.user.relationship.model.enums.RelationshipAction.ACCEPT;
import static com.almonium.user.relationship.model.enums.RelationshipAction.BLOCK;
import static com.almonium.user.relationship.model.enums.RelationshipAction.CANCEL;
import static com.almonium.user.relationship.model.enums.RelationshipAction.UNBLOCK;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FRIENDS;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.FST_BLOCKED_SND;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.MUTUAL_BLOCK;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.PENDING;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.SND_BLOCKED_FST;
import static com.almonium.user.relationship.model.enums.RelationshipStatus.UNFRIENDED;
import static com.almonium.user.relationship.service.RelationshipStateMachine.ActorRole.REQUESTEE;
import static com.almonium.user.relationship.service.RelationshipStateMachine.ActorRole.REQUESTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.almonium.user.relationship.exception.RelationshipException;
import org.junit.jupiter.api.Test;

class RelationshipStateMachineTest {
    private final RelationshipStateMachine stateMachine = new RelationshipStateMachine();

    @Test
    void recipientAcceptsPendingRequest() {
        assertThat(stateMachine.transition(PENDING, ACCEPT, REQUESTEE)).isEqualTo(FRIENDS);
    }

    @Test
    void recipientCannotCancelRequest() {
        assertThatThrownBy(() -> stateMachine.transition(PENDING, CANCEL, REQUESTEE))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("User is not the requester of this relationship");
    }

    @Test
    void secondBlockCreatesMutualBlock() {
        assertThat(stateMachine.transition(FST_BLOCKED_SND, BLOCK, REQUESTEE)).isEqualTo(MUTUAL_BLOCK);
    }

    @Test
    void sameUserCannotBlockTwice() {
        assertThatThrownBy(() -> stateMachine.transition(FST_BLOCKED_SND, BLOCK, REQUESTER))
                .isInstanceOf(RelationshipException.class)
                .hasMessage("Relationship is already blocked");
    }

    @Test
    void unblockingMutualBlockPreservesOtherUsersBlock() {
        assertThat(stateMachine.transition(MUTUAL_BLOCK, UNBLOCK, REQUESTER)).isEqualTo(SND_BLOCKED_FST);
        assertThat(stateMachine.transition(MUTUAL_BLOCK, UNBLOCK, REQUESTEE)).isEqualTo(FST_BLOCKED_SND);
    }

    @Test
    void unilateralBlockEndsAsUnfriendedWhenBlockerUnblocks() {
        assertThat(stateMachine.transition(FST_BLOCKED_SND, UNBLOCK, REQUESTER)).isEqualTo(UNFRIENDED);
    }
}

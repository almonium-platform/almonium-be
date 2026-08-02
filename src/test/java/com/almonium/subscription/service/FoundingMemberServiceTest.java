package com.almonium.subscription.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.subscription.model.entity.FoundingMember;
import com.almonium.subscription.repository.FoundingMemberRepository;
import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FoundingMemberServiceTest {
    @Mock
    FoundingMemberRepository repository;

    FoundingMemberService service;

    @BeforeEach
    void setUp() {
        service = new FoundingMemberService(repository);
    }

    @Test
    void reservesLowestAvailableSlotForNewFounder() {
        User user = User.builder().id(UUID.randomUUID()).build();
        FoundingMember slot = slot(1, null, FoundingMember.Status.AVAILABLE);
        when(repository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(repository.findFirstByStatusOrderBySlotNumber(FoundingMember.Status.AVAILABLE))
                .thenReturn(Optional.of(slot));

        assertThat(service.reserveForCheckout(user)).contains(1);
        assertThat(slot.getUser()).isSameAs(user);
        assertThat(slot.getStatus()).isEqualTo(FoundingMember.Status.RESERVED);
        assertThat(slot.getReservedAt()).isNotNull();
        verify(repository).save(slot);
    }

    @Test
    void confirmedFounderDoesNotReceiveFounderPriceAgain() {
        User user = User.builder().id(UUID.randomUUID()).build();
        FoundingMember slot = slot(1, user, FoundingMember.Status.CONFIRMED);
        when(repository.findByUserId(user.getId())).thenReturn(Optional.of(slot));

        assertThat(service.reserveForCheckout(user)).isEmpty();
    }

    @Test
    void rejectsConfirmationForDifferentUser() {
        User owner = User.builder().id(UUID.randomUUID()).build();
        FoundingMember slot = slot(1, owner, FoundingMember.Status.RESERVED);
        when(repository.findLockedBySlotNumber(1)).thenReturn(Optional.of(slot));

        assertThatThrownBy(() -> service.confirm(1, UUID.randomUUID(), "txn_01test", "sub_01test"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("another user");
    }

    private FoundingMember slot(int number, User user, FoundingMember.Status status) {
        FoundingMember slot = new FoundingMember();
        slot.setSlotNumber(number);
        slot.setUser(user);
        slot.setStatus(status);
        return slot;
    }
}

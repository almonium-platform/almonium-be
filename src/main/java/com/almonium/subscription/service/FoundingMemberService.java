package com.almonium.subscription.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.subscription.dto.response.FoundingMemberStatusDto;
import com.almonium.subscription.model.entity.FoundingMember;
import com.almonium.subscription.repository.FoundingMemberRepository;
import com.almonium.user.core.model.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class FoundingMemberService {
    public static final int CAPACITY = 50;

    FoundingMemberRepository foundingMemberRepository;

    @Transactional(readOnly = true)
    public FoundingMemberStatusDto status() {
        long claimed = foundingMemberRepository.countByStatusIn(
                List.of(FoundingMember.Status.RESERVED, FoundingMember.Status.CONFIRMED));
        return new FoundingMemberStatusDto(CAPACITY, claimed);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<Integer> reserveForCheckout(User user) {
        Optional<FoundingMember> existing = foundingMemberRepository.findByUserId(user.getId());
        if (existing.isPresent()) {
            FoundingMember allocation = existing.orElseThrow();
            return allocation.getStatus() == FoundingMember.Status.RESERVED
                    ? Optional.of(allocation.getSlotNumber())
                    : Optional.empty();
        }

        return foundingMemberRepository
                .findFirstByStatusOrderBySlotNumber(FoundingMember.Status.AVAILABLE)
                .map(slot -> {
                    slot.setUser(user);
                    slot.setStatus(FoundingMember.Status.RESERVED);
                    slot.setReservedAt(Instant.now());
                    foundingMemberRepository.save(slot);
                    return slot.getSlotNumber();
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void attachTransaction(int slotNumber, UUID userId, String transactionId) {
        FoundingMember slot = getLockedSlot(slotNumber);
        assertReservedForUser(slot, userId);
        slot.setPaddleTransactionId(transactionId);
        foundingMemberRepository.save(slot);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void releaseReservationAfterCheckoutFailure(int slotNumber, UUID userId) {
        FoundingMember slot = getLockedSlot(slotNumber);
        if (slot.getStatus() != FoundingMember.Status.RESERVED
                || slot.getUser() == null
                || !slot.getUser().getId().equals(userId)
                || slot.getPaddleTransactionId() != null) {
            return;
        }
        slot.setUser(null);
        slot.setStatus(FoundingMember.Status.AVAILABLE);
        slot.setReservedAt(null);
        foundingMemberRepository.save(slot);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void confirm(int slotNumber, UUID userId, String transactionId, String subscriptionId) {
        FoundingMember slot = getLockedSlot(slotNumber);
        if (slot.getStatus() == FoundingMember.Status.CONFIRMED) {
            assertOwnedByUser(slot, userId);
            return;
        }
        assertReservedForUser(slot, userId);
        if (slot.getPaddleTransactionId() != null
                && !slot.getPaddleTransactionId().equals(transactionId)) {
            throw new IllegalStateException("Founding-member transaction does not match the reservation");
        }
        slot.setPaddleTransactionId(transactionId);
        slot.setPaddleSubscriptionId(subscriptionId);
        slot.setStatus(FoundingMember.Status.CONFIRMED);
        slot.setConfirmedAt(Instant.now());
        foundingMemberRepository.save(slot);
    }

    private FoundingMember getLockedSlot(int slotNumber) {
        return foundingMemberRepository
                .findLockedBySlotNumber(slotNumber)
                .orElseThrow(() -> new IllegalStateException("Founding-member slot not found: " + slotNumber));
    }

    private void assertReservedForUser(FoundingMember slot, UUID userId) {
        if (slot.getStatus() != FoundingMember.Status.RESERVED) {
            throw new IllegalStateException("Founding-member slot is not reserved");
        }
        assertOwnedByUser(slot, userId);
    }

    private void assertOwnedByUser(FoundingMember slot, UUID userId) {
        if (slot.getUser() == null || !slot.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Founding-member slot belongs to another user");
        }
    }
}

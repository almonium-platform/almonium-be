package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.FoundingMember;
import jakarta.persistence.LockModeType;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface FoundingMemberRepository extends JpaRepository<FoundingMember, Integer> {
    long countByStatusIn(Collection<FoundingMember.Status> statuses);

    Optional<FoundingMember> findByUserId(UUID userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FoundingMember> findFirstByStatusOrderBySlotNumber(FoundingMember.Status status);
}

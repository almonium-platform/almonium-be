package com.almonium.card.deck.repository;

import com.almonium.card.deck.model.entity.Deck;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck, UUID> {
    Optional<Deck> findByShareId(String shareId);

    boolean existsByShareId(String shareId);

    Optional<Deck> findByIdAndOwnerUserIdAndDeletedAtIsNull(UUID id, UUID userId);

    List<Deck> findAllByOwnerUserIdAndDeletedAtIsNullOrderByUpdatedAtDesc(UUID userId);
}

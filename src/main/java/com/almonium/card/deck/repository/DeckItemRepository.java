package com.almonium.card.deck.repository;

import com.almonium.card.deck.model.entity.Deck;
import com.almonium.card.deck.model.entity.DeckItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckItemRepository extends JpaRepository<DeckItem, UUID> {
    List<DeckItem> findAllByDeckOrderByPositionAsc(Deck deck);

    void deleteAllByDeck(Deck deck);
}

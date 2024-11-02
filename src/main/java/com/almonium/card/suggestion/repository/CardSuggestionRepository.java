package com.almonium.card.suggestion.repository;

import com.almonium.card.core.model.entity.Card;
import com.almonium.card.suggestion.model.entity.CardSuggestion;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardSuggestionRepository extends JpaRepository<CardSuggestion, Long> {
    CardSuggestion getBySenderAndRecipientAndCard(Learner sender, Learner recipient, Card card);

    List<CardSuggestion> getByRecipient(Learner recipient);
}

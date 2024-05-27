package com.linguarium.card.suggestion.repository;

import com.linguarium.card.core.model.Card;
import com.linguarium.card.suggestion.model.CardSuggestion;
import com.linguarium.user.core.model.Learner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardSuggestionRepository extends JpaRepository<CardSuggestion, Long> {
    CardSuggestion getBySenderAndRecipientAndCard(Learner sender, Learner recipient, Card card);

    List<CardSuggestion> getByRecipient(Learner recipient);
}

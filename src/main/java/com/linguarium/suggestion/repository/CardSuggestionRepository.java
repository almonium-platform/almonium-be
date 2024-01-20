package com.linguarium.suggestion.repository;

import com.linguarium.card.model.Card;
import com.linguarium.suggestion.model.CardSuggestion;
import com.linguarium.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardSuggestionRepository extends JpaRepository<CardSuggestion, Long> {

    CardSuggestion getBySenderAndRecipientAndCard(User sender, User recipient, Card card);
    List<CardSuggestion> getByRecipient(User recipient);
}

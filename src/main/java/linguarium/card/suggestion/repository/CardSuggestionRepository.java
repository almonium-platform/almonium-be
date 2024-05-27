package linguarium.card.suggestion.repository;

import java.util.List;
import linguarium.card.core.model.Card;
import linguarium.card.suggestion.model.CardSuggestion;
import linguarium.user.core.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardSuggestionRepository extends JpaRepository<CardSuggestion, Long> {
    CardSuggestion getBySenderAndRecipientAndCard(Learner sender, Learner recipient, Card card);

    List<CardSuggestion> getByRecipient(Learner recipient);
}

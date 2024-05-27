package linguarium.card.suggestion.service;

import java.util.List;
import linguarium.card.core.dto.CardDto;
import linguarium.card.suggestion.dto.CardSuggestionDto;
import linguarium.user.core.model.entity.Learner;

public interface CardSuggestionService {
    List<CardDto> getSuggestedCards(Learner user);

    void declineSuggestion(Long id, Learner recipient);

    void acceptSuggestion(Long id, Learner recipient);

    boolean suggestCard(CardSuggestionDto dto, Learner sender);
}

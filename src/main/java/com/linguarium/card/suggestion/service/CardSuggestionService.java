package com.linguarium.card.suggestion.service;

import com.linguarium.card.core.dto.CardDto;
import com.linguarium.card.suggestion.dto.CardSuggestionDto;
import com.linguarium.user.core.model.Learner;
import java.util.List;

public interface CardSuggestionService {
    List<CardDto> getSuggestedCards(Learner user);

    void declineSuggestion(Long id, Learner recipient);

    void acceptSuggestion(Long id, Learner recipient);

    boolean suggestCard(CardSuggestionDto dto, Learner sender);
}

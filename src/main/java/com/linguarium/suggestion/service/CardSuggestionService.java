package com.linguarium.suggestion.service;

import com.linguarium.card.dto.CardDto;
import com.linguarium.suggestion.dto.CardSuggestionDto;
import com.linguarium.user.model.Learner;
import java.util.List;

public interface CardSuggestionService {
    List<CardDto> getSuggestedCards(Learner user);

    void declineSuggestion(Long id, Learner recipient);

    void acceptSuggestion(Long id, Learner recipient);

    boolean suggestCard(CardSuggestionDto dto, Learner sender);
}

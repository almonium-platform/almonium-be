package com.almonium.card.suggestion.service;

import com.almonium.card.core.dto.CardDto;
import com.almonium.card.suggestion.dto.CardSuggestionDto;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;

public interface CardSuggestionService {
    List<CardDto> getSuggestedCards(Learner user);

    void declineSuggestion(Long id, Learner recipient);

    void acceptSuggestion(Long id, Learner recipient);

    boolean suggestCard(CardSuggestionDto dto, Learner sender);
}

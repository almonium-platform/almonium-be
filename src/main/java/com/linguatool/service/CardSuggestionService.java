package com.linguatool.service;

import com.linguatool.model.dto.CardAcceptanceDto;
import com.linguatool.model.dto.CardSuggestionDto;
import com.linguatool.model.dto.external_api.request.CardDto;
import com.linguatool.model.entity.user.User;

import java.util.List;

public interface CardSuggestionService {
    List<CardDto> getSuggestedCards(User user);

    void declineSuggestion(CardAcceptanceDto dto, User recipient);

    void acceptSuggestion(CardAcceptanceDto dto, User recipient);

    boolean suggestCard(CardSuggestionDto dto, User sender);
}

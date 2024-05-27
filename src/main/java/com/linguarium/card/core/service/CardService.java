package com.linguarium.card.core.service;

import com.linguarium.card.core.dto.CardCreationDto;
import com.linguarium.card.core.dto.CardDto;
import com.linguarium.card.core.dto.CardUpdateDto;
import com.linguarium.user.core.model.Learner;
import java.util.List;

public interface CardService {
    CardDto getCardById(Long id);

    CardDto getCardByPublicId(String hash);

    List<CardDto> getUsersCards(Learner learner);

    void createCard(Learner learner, CardCreationDto dto);

    List<CardDto> searchByEntry(String entry, Learner learner);

    void updateCard(Long id, CardUpdateDto dto, Learner learner);

    List<CardDto> getUsersCardsOfLang(String code, Learner user);

    void deleteById(Long id);
}

package com.almonium.card.core.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.CardCreationDto;
import com.almonium.card.core.dto.CardDto;
import com.almonium.card.core.dto.CardUpdateDto;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;

public interface CardService {
    CardDto getCardById(Long id);

    CardDto getCardByPublicId(String hash);

    List<CardDto> getUsersCards(Learner learner);

    void createCard(Learner learner, CardCreationDto dto);

    List<CardDto> searchByEntry(String entry, Learner learner);

    void updateCard(Long id, CardUpdateDto dto, Learner learner);

    List<CardDto> getUsersCardsOfLang(Language code, Learner user);

    void deleteById(Long id);
}

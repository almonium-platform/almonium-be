package com.linguarium.card.service;

import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.user.model.Learner;

import java.util.List;

public interface CardService {

    CardDto getCardById(Long id);

    CardDto getCardByPublicId(String hash);

    List<CardDto> getUsersCards(Learner learner);

    void createCard(Learner learner, CardCreationDto dto);

    List<CardDto> searchByEntry(String entry, Learner learner);

    void updateCard(CardUpdateDto dto, Learner learner);

    List<CardDto> getUsersCardsOfLang(String code, Learner user);

    void deleteById(Long id);
}

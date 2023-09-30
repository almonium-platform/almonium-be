package com.linguarium.card.service;

import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.user.model.User;

import java.util.List;

public interface CardService {

    CardDto getCardById(Long id);

    CardDto getCardByPublicId(String hash);

    List<CardDto> getUsersCards(User user);

    void createCard(User user, CardCreationDto dto);

    List<CardDto> searchByEntry(String entry, User user);

    void updateCard(CardUpdateDto dto, User user);

    List<CardDto> getUsersCardsOfLang(String code, User user);

    void deleteById(Long id);
}

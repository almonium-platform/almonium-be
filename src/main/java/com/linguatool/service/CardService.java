package com.linguatool.service;

import com.linguatool.model.dto.external_api.request.CardCreationDto;
import com.linguatool.model.dto.external_api.request.CardDto;
import com.linguatool.model.dto.external_api.request.CardUpdateDto;
import com.linguatool.model.entity.user.User;

import java.util.List;

public interface CardService {

    CardDto getCardById(Long id);

    CardDto getCardByHash(String hash);

    List<CardDto> getUsersCards(User user);

    void createCard(User user, CardCreationDto dto);

    List<CardDto> searchByEntry(String entry, User user);

    void updateCard(CardUpdateDto dto, User user);

    List<CardDto> getUsersCardsOfLang(String code, User user);
    void deleteById(Long id);
}

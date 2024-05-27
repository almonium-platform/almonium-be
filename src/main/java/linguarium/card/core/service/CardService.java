package linguarium.card.core.service;

import java.util.List;
import linguarium.card.core.dto.CardCreationDto;
import linguarium.card.core.dto.CardDto;
import linguarium.card.core.dto.CardUpdateDto;
import linguarium.user.core.model.entity.Learner;

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

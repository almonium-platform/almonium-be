package com.linguarium.suggestion.service.impl;

import com.linguarium.card.dto.CardDto;
import com.linguarium.card.mapper.CardMapper;
import com.linguarium.card.model.Card;
import com.linguarium.card.model.Example;
import com.linguarium.card.model.Translation;
import com.linguarium.card.repository.CardRepository;
import com.linguarium.card.repository.ExampleRepository;
import com.linguarium.card.repository.TranslationRepository;
import com.linguarium.suggestion.dto.CardAcceptanceDto;
import com.linguarium.suggestion.dto.CardSuggestionDto;
import com.linguarium.suggestion.model.CardSuggestion;
import com.linguarium.suggestion.repository.CardSuggestionRepository;
import com.linguarium.suggestion.service.CardSuggestionService;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CardSuggestionServiceImpl implements CardSuggestionService {
    CardRepository cardRepository;
    CardSuggestionRepository cardSuggestionRepository;
    ExampleRepository exampleRepository;
    TranslationRepository translationRepository;
    UserRepository userRepository;
    CardMapper cardMapper;

    @Transactional
    public void cloneCard(Card entity, User user) {
        Card card = cardMapper.copyCardDtoToEntity(cardMapper.cardEntityToDto(entity));

        user.getLearner().addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(e -> e.setCard(card));

        List<Translation> translations = card.getTranslations();

        cardRepository.save(card);
        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        userRepository.save(user);
        log.info("Cloned card {} for user {}", card, user);
    }

    @Override
    @Transactional
    public List<CardDto> getSuggestedCards(User user) {
        return cardSuggestionRepository.getByRecipient(user)
                .stream()
                .map(sug -> {
                    CardDto dto = cardMapper.cardEntityToDto(sug.getCard());
                    dto.setUserId(sug.getSender().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void declineSuggestion(CardAcceptanceDto dto, User recipient) {
        //TODO Test, changed repo name - entity to just ID
        cardSuggestionRepository
                .deleteBySenderIdAndRecipientIdAndCardId(
                        dto.getSenderId(),
                        recipient.getId(),
                        dto.getCardId()
                );
    }

    @Override
    @Transactional
    public void acceptSuggestion(CardAcceptanceDto dto, User recipient) {
        User sender = userRepository.getById(dto.getSenderId());
        Card card = cardRepository.getById(dto.getCardId());
        cloneCard(card, recipient);
        CardSuggestion cardSuggestion = cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card);
        cardSuggestionRepository.delete(cardSuggestion);
    }

    @Override
    public boolean suggestCard(CardSuggestionDto dto, User sender) {
        Card card = cardRepository.getById(dto.getCardId());
        User recipient = userRepository.getById(dto.getRecipientId());
        //TODO  notifications
        //TODO check if has access
        if (cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card) == null) {
            cardSuggestionRepository.save(new CardSuggestion(sender, recipient, card));
            return true;
        } else {
            return false;
        }
    }
}

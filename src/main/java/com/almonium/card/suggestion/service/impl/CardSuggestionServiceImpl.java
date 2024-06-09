package com.almonium.card.suggestion.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.dto.CardDto;
import com.almonium.card.core.mapper.CardMapper;
import com.almonium.card.core.model.entity.Card;
import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.repository.CardRepository;
import com.almonium.card.core.repository.ExampleRepository;
import com.almonium.card.core.repository.TranslationRepository;
import com.almonium.card.suggestion.dto.CardSuggestionDto;
import com.almonium.card.suggestion.model.entity.CardSuggestion;
import com.almonium.card.suggestion.repository.CardSuggestionRepository;
import com.almonium.card.suggestion.service.CardSuggestionService;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Transactional
public class CardSuggestionServiceImpl implements CardSuggestionService {
    CardRepository cardRepository;
    CardSuggestionRepository cardSuggestionRepository;
    ExampleRepository exampleRepository;
    TranslationRepository translationRepository;
    LearnerRepository learnerRepository;
    CardMapper cardMapper;

    @Override
    public List<CardDto> getSuggestedCards(Learner user) {
        return cardSuggestionRepository.getByRecipient(user).stream()
                .map(sug -> {
                    CardDto dto = cardMapper.cardEntityToDto(sug.getCard());
                    dto.setUserId(sug.getSender().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void declineSuggestion(Long id, Learner actionExecutor) {
        CardSuggestion cardSuggestion = getCardSuggestion(id);
        Learner recipient = cardSuggestion.getRecipient();
        checkAuthorization(actionExecutor, recipient);
        cardSuggestionRepository.deleteById(id);
    }

    @Override
    public void acceptSuggestion(Long id, Learner actionExecutor) {
        CardSuggestion cardSuggestion = getCardSuggestion(id);
        Learner recipient = cardSuggestion.getRecipient();
        checkAuthorization(actionExecutor, recipient);
        cloneCard(cardSuggestion.getCard(), recipient);
        cardSuggestionRepository.delete(cardSuggestion);
    }

    @Override
    public boolean suggestCard(CardSuggestionDto dto, Learner sender) {
        Card card = cardRepository.findById(dto.cardId()).orElseThrow();
        Learner recipient = learnerRepository.findById(dto.recipientId()).orElseThrow();
        // TODO  notifications
        // TODO check if has access
        if (cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card) != null) {
            return false;
        }
        cardSuggestionRepository.save(new CardSuggestion(sender, recipient, card));
        return true;
    }

    private void cloneCard(Card entity, Learner user) {
        Card card = cardMapper.copyCardDtoToEntity(cardMapper.cardEntityToDto(entity));

        user.addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(example -> example.setCard(card));

        List<Translation> translations = card.getTranslations();

        cardRepository.save(card);
        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        learnerRepository.save(user);
        log.info("Cloned card {} for user {}", card, user);
    }

    @SneakyThrows
    private void checkAuthorization(Learner actionExecutor, Learner recipient) {
        if (!recipient.equals(actionExecutor)) {
            throw new IllegalAccessException("You aren't authorized to act on behalf of other userInfo");
        }
    }

    private CardSuggestion getCardSuggestion(Long id) {
        return cardSuggestionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card suggestion with ID " + id + " not found"));
    }
}

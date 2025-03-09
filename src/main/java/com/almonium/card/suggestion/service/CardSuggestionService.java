package com.almonium.card.suggestion.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.mapper.CardMapper;
import com.almonium.card.core.model.entity.Card;
import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.repository.CardRepository;
import com.almonium.card.core.repository.ExampleRepository;
import com.almonium.card.core.repository.TranslationRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.card.suggestion.dto.request.CardSuggestionDto;
import com.almonium.card.suggestion.model.entity.CardSuggestion;
import com.almonium.card.suggestion.repository.CardSuggestionRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
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
public class CardSuggestionService {
    LearnerFinder learnerFinder;

    CardRepository cardRepository;
    CardSuggestionRepository cardSuggestionRepository;
    ExampleRepository exampleRepository;
    TranslationRepository translationRepository;
    LearnerRepository learnerRepository;

    CardMapper cardMapper;

    public List<CardDto> getSuggestedCards(User user, Language lang) {
        Learner learner = learnerFinder.findLearner(user, lang);

        return cardSuggestionRepository.getByRecipient(learner).stream()
                .map(sug -> {
                    CardDto dto = cardMapper.cardEntityToDto(sug.getCard());
                    dto.setUserId(sug.getSender().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public void declineSuggestion(UUID id, User actionExecutor) {
        CardSuggestion cardSuggestion = getCardSuggestion(id);
        User recipient = cardSuggestion.getRecipient().getUser();
        checkAuthorization(actionExecutor, recipient);
        cardSuggestionRepository.deleteById(id);
    }

    public void acceptSuggestion(UUID id, User actionExecutor) {
        CardSuggestion cardSuggestion = getCardSuggestion(id);
        User recipient = cardSuggestion.getRecipient().getUser();
        checkAuthorization(actionExecutor, recipient);
        Learner recipientLearner =
                learnerFinder.findLearner(recipient, cardSuggestion.getCard().getLanguage());
        cloneCard(cardSuggestion.getCard(), recipientLearner);
        cardSuggestionRepository.delete(cardSuggestion);
    }

    public boolean suggestCard(CardSuggestionDto dto, User user) {
        Card card = cardRepository.findById(dto.cardId()).orElseThrow();
        Learner sender = learnerFinder.findLearner(user, card.getLanguage());
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
    private void checkAuthorization(User actionExecutor, User recipient) {
        if (!recipient.equals(actionExecutor)) {
            throw new IllegalAccessException("You aren't authorized to act on behalf of other userInfo");
        }
    }

    private CardSuggestion getCardSuggestion(UUID id) {
        return cardSuggestionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Card suggestion with ID " + id + " not found"));
    }
}

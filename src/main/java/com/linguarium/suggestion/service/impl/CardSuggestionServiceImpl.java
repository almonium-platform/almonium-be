package com.linguarium.suggestion.service.impl;

import com.linguarium.card.dto.CardDto;
import com.linguarium.card.mapper.CardMapper;
import com.linguarium.card.model.Card;
import com.linguarium.card.model.Example;
import com.linguarium.card.model.Translation;
import com.linguarium.card.repository.CardRepository;
import com.linguarium.card.repository.ExampleRepository;
import com.linguarium.card.repository.TranslationRepository;
import com.linguarium.suggestion.dto.CardSuggestionDto;
import com.linguarium.suggestion.model.CardSuggestion;
import com.linguarium.suggestion.repository.CardSuggestionRepository;
import com.linguarium.suggestion.service.CardSuggestionService;
import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    LearnerRepository learnerRepository;
    CardMapper cardMapper;

    @Transactional
    public void cloneCard(Card entity, Learner user) {
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

    @Override
    @Transactional
    public List<CardDto> getSuggestedCards(Learner user) {
        return cardSuggestionRepository.getByRecipient(user)
                .stream()
                .map(sug -> {
                    CardDto dto = cardMapper.cardEntityToDto(sug.getCard());
                    dto.setUserId(sug.getSender().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    @Transactional
    public void declineSuggestion(Long id, Learner actionExecutor) {
        CardSuggestion cardSuggestion = cardSuggestionRepository.findById(id).orElseThrow();
        Learner recipient = cardSuggestion.getRecipient();
        if (!recipient.equals(actionExecutor)) {
            throw new IllegalAccessException("You aren't authorized to act on behalf of other user");
        }
        cardSuggestionRepository.deleteById(id);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void acceptSuggestion(Long id, Learner actionExecutor) {
        CardSuggestion cardSuggestion = cardSuggestionRepository.findById(id).orElseThrow();
        Learner recipient = cardSuggestion.getRecipient();
        if (!recipient.equals(actionExecutor)) {
            throw new IllegalAccessException("You aren't authorized to act on behalf of other user");
        }

        cloneCard(cardSuggestion.getCard(), recipient);
        cardSuggestionRepository.delete(cardSuggestion);
    }

    @Override
    public boolean suggestCard(CardSuggestionDto dto, Learner sender) {
        Card card = cardRepository.findById(dto.cardId()).orElseThrow();
        Learner recipient = learnerRepository.findById(dto.recipientId()).orElseThrow();
        //TODO  notifications
        //TODO check if has access
        if (cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card) != null) {
            return false;
        }
        cardSuggestionRepository.save(new CardSuggestion(sender, recipient, card));
        return true;
    }
}

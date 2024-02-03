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
import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CardSuggestionServiceTest {
    @Mock
    CardRepository cardRepository;
    @Mock
    CardSuggestionRepository cardSuggestionRepository;
    @Mock
    ExampleRepository exampleRepository;
    @Mock
    TranslationRepository translationRepository;
    @Mock
    LearnerRepository learnerRepository;
    @Mock
    CardMapper cardMapper;
    @InjectMocks
    CardSuggestionServiceImpl cardSuggestionService;

    @DisplayName("Should return list of suggested CardDto")
    @Test
    void givenUser_whenGetSuggestedCards_thenReturnListOfCardDto() {
        // Arrange
        Learner user = new Learner();
        Long id = 1L;
        Learner sender = Learner.builder().id(id).build();
        Card card = Card.builder().id(id).build();
        List<CardSuggestion> suggestions = Collections.singletonList(new CardSuggestion(sender, user, card));
        CardDto expectedDto = CardDto.builder().userId(sender.getId()).build();

        when(cardSuggestionRepository.getByRecipient(user)).thenReturn(suggestions);
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        List<CardDto> result = cardSuggestionService.getSuggestedCards(user);

        // Assert
        assertThat(result).containsExactly(expectedDto);
    }

    @DisplayName("Should delete the suggestion")
    @Test
    void givenCardAcceptanceDtoAndUser_whenDeclineSuggestion_thenSuggestionDeleted() {
        // Arrange
        //TODO provoke illegalAccess
        Long suggestionId = 3L;
        Long userId = 4L;

        Learner recipient = Learner.builder().id(userId).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(Card.builder().build())
                .recipient(recipient)
                .build();
        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Act
        cardSuggestionService.declineSuggestion(suggestionId, recipient);

        // Assert
        verify(cardSuggestionRepository).deleteById(suggestionId);
    }

    @DisplayName("Should accept a card suggestion and clone the card")
    @Test
    void givenCardAcceptanceDtoAndRecipient_whenAcceptSuggestion_thenCloneCardAndDeleteSuggestion() {
        // Arrange
        Long suggestionId = 3L;
        Long userId = 4L;
        Card card = Card.builder().id(1L).build();
        Learner recipient = Learner.builder().id(userId).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(card)
                .recipient(recipient)
                .build();
        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Create a spy of your service
        CardSuggestionServiceImpl cardServiceSpy = spy(cardSuggestionService);

        // Mock the cloneCard method to do nothing
        doNothing().when(cardServiceSpy).cloneCard(any(Card.class), any(Learner.class));

        // Act
        cardServiceSpy.acceptSuggestion(suggestionId, recipient);

        // Assert
        verify(cardServiceSpy).cloneCard(card, recipient);
        verify(cardSuggestionRepository).delete(cardSuggestion);
    }

    @DisplayName("Should clone a card and save it along with its examples, translations, and tags")
    @Test
    void givenCardAndUser_whenCloneCard_thenSaveClonedCardAndRelatedEntities() {
        // Arrange
        Card card = new Card();

        List<Example> examples = Arrays.asList(
                new Example(1L, "example1", "translation1", card),
                new Example(2L, "example2", "translation2", card)
        );
        List<Translation> translations = Arrays.asList(
                new Translation(3L, "translation1", card),
                new Translation(4L, "translation2", card)
        );

        card = Card.builder()
                .examples(examples)
                .translations(translations)
                .cardTags(new HashSet<>())
                .build();

        Learner user = Learner.builder()
                .cards(new HashSet<>())
                .build();

        CardDto cardDto = new CardDto(); // Initialize as needed
        when(cardMapper.cardEntityToDto(card)).thenReturn(cardDto);
        when(cardMapper.copyCardDtoToEntity(eq(cardDto))).thenReturn(card);

        // Act
        cardSuggestionService.cloneCard(card, user);

        // Assert
        verify(cardRepository).save(argThat(savedCard ->
                savedCard.getExamples().equals(examples)
                        && savedCard.getTranslations().equals(translations)
        ));
        verify(translationRepository).saveAll(eq(translations));
        verify(exampleRepository).saveAll(eq(examples));
        verify(learnerRepository).save(eq(user));
    }

    @DisplayName("Should save a card suggestion and return true if it doesn't exist")
    @Test
    void givenNewCardSuggestionDtoAndSender_whenSuggestCard_thenReturnTrueAndSaveIt() {
        // Arrange
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        Learner sender = new Learner();
        Learner recipient = new Learner();
        Card card = Card.builder().id(2L)
                .examples(new ArrayList<>())
                .translations(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.findById(dto.cardId())).thenReturn(Optional.of(card));
        when(learnerRepository.findById(dto.recipientId())).thenReturn(Optional.of(recipient));
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card)).thenReturn(null);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, sender);

        // Assert
        assertThat(result).isTrue();
        verify(cardSuggestionRepository).save(argThat(suggestion ->
                suggestion.getSender().equals(sender)
                        && suggestion.getRecipient().equals(recipient)
                        && suggestion.getCard().equals(card)
        ));
    }

    @DisplayName("Should not save a card suggestion and return false if it already exists")
    @Test
    void givenExistingCardSuggestionDtoAndSender_whenSuggestCard_thenReturnFalseAndDoNotSaveIt() {
        // Arrange
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        Learner sender = new Learner();
        Learner recipient = new Learner();
        Card card = new Card();
        CardSuggestion existingSuggestion = new CardSuggestion(sender, recipient, card);

        when(cardRepository.findById(dto.cardId())).thenReturn(Optional.of(card));
        when(learnerRepository.findById(dto.recipientId())).thenReturn(Optional.of(recipient));
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card))
                .thenReturn(existingSuggestion);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, sender);

        // Assert
        assertThat(result).isFalse();
        verify(cardSuggestionRepository, never()).save(any(CardSuggestion.class));
    }
}

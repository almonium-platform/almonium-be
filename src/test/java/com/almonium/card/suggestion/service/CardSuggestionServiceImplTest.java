package com.almonium.card.suggestion.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.almonium.card.suggestion.service.impl.CardSuggestionServiceImpl;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class CardSuggestionServiceImplTest {
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

    @DisplayName("Should throw IllegalAccessException on accepting suggestion with unauthorized user")
    @Test
    void givenUnauthorizedUserWhenAcceptSuggestionThenThrowIllegalAccessException() {
        // Arrange
        Long suggestionId = 3L;
        Long executorId = 4L; // Action executor's ID
        Long recipientId = 5L; // Recipient's ID, different from the executor to provoke illegal access
        Learner executor = Learner.builder().id(executorId).build();
        Learner recipient = Learner.builder().id(recipientId).build();
        CardSuggestion cardSuggestion =
                CardSuggestion.builder().id(suggestionId).recipient(recipient).build();
        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));
        // Act & Assert
        assertThatThrownBy(() -> cardSuggestionService.acceptSuggestion(suggestionId, executor))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining("You aren't authorized to act on behalf of other userInfo");
    }

    @DisplayName("Should decline and delete the suggestion")
    @Test
    void givenCardAcceptanceDtoAndUser_whenDeclineSuggestion_thenSuggestionDeleted() {
        // Arrange
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

    @DisplayName("Should throw IllegalAccessException on declining suggestion with unauthorized user")
    @Test
    void givenUnauthorizedUser_whenDeclineSuggestion_thenThrowIllegalAccessException() {
        // Arrange
        Long suggestionId = 3L;
        Long executorId = 4L; // Action executor's ID
        Long recipientId = 5L; // Recipient's ID, different from the executor to provoke illegal access

        Learner executor = Learner.builder().id(executorId).build();
        Learner recipient = Learner.builder().id(recipientId).build();

        CardSuggestion cardSuggestion =
                CardSuggestion.builder().id(suggestionId).recipient(recipient).build();
        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Act & Assert
        assertThatThrownBy(() -> cardSuggestionService.declineSuggestion(suggestionId, executor))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining("You aren't authorized to act on behalf of other userInfo");
    }

    @DisplayName("Should clone a card and save it along with its examples, translations, and tags")
    @Test
    void givenCardAndUser_whenCloneCard_thenSaveClonedCardAndRelatedEntities() {
        // Arrange
        Card card = new Card();

        List<Example> examples = Arrays.asList(
                new Example(1L, "example1", "translation1", card), new Example(2L, "example2", "translation2", card));
        List<Translation> translations =
                Arrays.asList(new Translation(3L, "translation1", card), new Translation(4L, "translation2", card));

        card = Card.builder()
                .examples(examples)
                .translations(translations)
                .cardTags(new HashSet<>())
                .build();

        // Arrange
        Long suggestionId = 3L;
        Long userId = 4L;
        Learner recipient = Learner.builder().id(userId).cards(new HashSet<>()).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(card)
                .recipient(recipient)
                .build();

        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        CardDto cardDto = new CardDto(); // Initialize as needed
        when(cardMapper.cardEntityToDto(card)).thenReturn(cardDto);
        when(cardMapper.copyCardDtoToEntity(eq(cardDto))).thenReturn(card);

        // Act
        cardSuggestionService.acceptSuggestion(suggestionId, recipient);

        // Assert
        verify(cardRepository)
                .save(argThat(savedCard -> savedCard.getExamples().equals(examples)
                        && savedCard.getTranslations().equals(translations)));
        verify(translationRepository).saveAll(eq(translations));
        verify(exampleRepository).saveAll(eq(examples));
        verify(learnerRepository).save(eq(recipient));
        verify(cardSuggestionRepository).delete(cardSuggestion);
    }

    @DisplayName("Should save a card suggestion and return true if it doesn't exist")
    @Test
    void givenNewCardSuggestionDtoAndSender_whenSuggestCard_thenReturnTrueAndSaveIt() {
        // Arrange
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        Learner sender = new Learner();
        Learner recipient = new Learner();
        Card card = Card.builder()
                .id(2L)
                .examples(new ArrayList<>())
                .translations(new ArrayList<>())
                .cardTags(Set.of())
                .build();

        when(cardRepository.findById(dto.cardId())).thenReturn(Optional.of(card));
        when(learnerRepository.findById(dto.recipientId())).thenReturn(Optional.of(recipient));
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card))
                .thenReturn(null);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, sender);

        // Assert
        assertThat(result).isTrue();
        verify(cardSuggestionRepository)
                .save(argThat(suggestion -> suggestion.getSender().equals(sender)
                        && suggestion.getRecipient().equals(recipient)
                        && suggestion.getCard().equals(card)));
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

    @DisplayName("Should throw EntityNotFoundException when CardSuggestion not found")
    @Test
    void givenNonExistingCardSuggestionId_whenGetCardSuggestion_thenThrowEntityNotFoundException() {
        // Arrange
        Long nonExistingId = 99L;
        when(cardSuggestionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cardSuggestionService.acceptSuggestion(nonExistingId, new Learner()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Card suggestion with ID " + nonExistingId + " not found");
    }
}

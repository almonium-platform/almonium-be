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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
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
    // IMPORTANT: We now need the LearnerFinder
    @Mock
    LearnerFinder learnerFinder;

    @InjectMocks
    CardSuggestionService cardSuggestionService;

    @DisplayName("Should return list of suggested CardDto")
    @Test
    void givenUserAndLanguage_whenGetSuggestedCards_thenReturnListOfCardDto() {
        // Arrange
        User user = User.builder().id(101L).build();
        Language lang = Language.EN;

        Learner recipientLearner = Learner.builder().id(201L).build();
        // The service calls learnerFinder to find the recipient Learner
        when(learnerFinder.findLearner(user, lang)).thenReturn(recipientLearner);

        Long cardId = 1L;
        Learner sender = Learner.builder().id(999L).build();
        Card card = Card.builder().id(cardId).build();

        // The CardSuggestion references the recipientLearner
        List<CardSuggestion> suggestions =
                Collections.singletonList(new CardSuggestion(sender, recipientLearner, card));

        CardDto expectedDto = CardDto.builder().userId(sender.getId()).build();

        when(cardSuggestionRepository.getByRecipient(recipientLearner)).thenReturn(suggestions);
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        List<CardDto> result = cardSuggestionService.getSuggestedCards(user, lang);

        // Assert
        assertThat(result).containsExactly(expectedDto);
    }

    @DisplayName("Should throw IllegalAccessException on accepting suggestion with unauthorized user")
    @Test
    void givenUnauthorizedUserWhenAcceptSuggestionThenThrowIllegalAccessException() {
        // Arrange
        Long suggestionId = 3L;
        // We'll have two User objects:
        Long executorId = 4L; // Action executor's ID
        Long recipientId = 5L; // Recipient's user ID (different from the executor -> triggers illegal access)

        User executorUser = User.builder().id(executorId).build();
        User recipientUser = User.builder().id(recipientId).build();

        // The recipient Learner has user = recipientUser
        Learner recipientLearner = Learner.builder().user(recipientUser).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .recipient(recipientLearner)
                .build();

        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Act & Assert
        assertThatThrownBy(() -> cardSuggestionService.acceptSuggestion(suggestionId, executorUser))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining("You aren't authorized to act on behalf of other userInfo");
    }

    @DisplayName("Should decline and delete the suggestion")
    @Test
    void givenExistingSuggestionAndAuthorizedUser_whenDeclineSuggestion_thenSuggestionDeleted() throws Exception {
        // Arrange
        Long suggestionId = 3L;
        UUID userId = 4L;
        // This is the user performing the action
        User actionExecutor = User.builder().id(userId).build();

        // The recipient Learner has user = actionExecutor
        Learner recipientLearner = Learner.builder().user(actionExecutor).build();

        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(Card.builder().build())
                .recipient(recipientLearner)
                .build();

        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Act
        cardSuggestionService.declineSuggestion(suggestionId, actionExecutor);

        // Assert
        verify(cardSuggestionRepository).deleteById(suggestionId);
    }

    @DisplayName("Should throw IllegalAccessException on declining suggestion with unauthorized user")
    @Test
    void givenUnauthorizedUser_whenDeclineSuggestion_thenThrowIllegalAccessException() {
        // Arrange
        Long suggestionId = 3L;
        Long executorId = 4L; // Action executor's ID
        Long recipientId = 5L; // Recipient's user ID

        User executorUser = User.builder().id(executorId).build();
        User recipientUser = User.builder().id(recipientId).build();

        Learner recipientLearner = Learner.builder().user(recipientUser).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .recipient(recipientLearner)
                .build();

        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Act & Assert
        assertThatThrownBy(() -> cardSuggestionService.declineSuggestion(suggestionId, executorUser))
                .isInstanceOf(IllegalAccessException.class)
                .hasMessageContaining("You aren't authorized to act on behalf of other userInfo");
    }

    @DisplayName("Should clone a card and save it along with its examples, translations, and tags")
    @Test
    void givenCardSuggestionAndAuthorizedUser_whenAcceptSuggestion_thenCloneCardAndSave() throws Exception {
        // Arrange
        Long suggestionId = 3L;
        UUID userId = 4L;
        User actionExecutor = User.builder().id(userId).build();

        // The recipient Learner with that user
        Learner recipientLearner = Learner.builder().user(actionExecutor).build();

        // Card to clone
        Card originalCard = Card.builder().id(999L).build();
        List<Example> examples = Arrays.asList(
                new Example(1L, "example1", "translation1", originalCard),
                new Example(2L, "example2", "translation2", originalCard));
        List<Translation> translations = Arrays.asList(
                new Translation(3L, "translationA", originalCard), new Translation(4L, "translationB", originalCard));
        originalCard.setExamples(examples);
        originalCard.setTranslations(translations);

        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(originalCard)
                .recipient(recipientLearner)
                .build();

        when(cardSuggestionRepository.findById(suggestionId)).thenReturn(Optional.of(cardSuggestion));

        // Mock copying the card
        CardDto originalCardDto = new CardDto();
        when(cardMapper.cardEntityToDto(originalCard)).thenReturn(originalCardDto);

        // We want the mapper to return a brand new Card entity with same data.
        // For simplicity, let's just return the same "originalCard" reference or a new instance.
        // Typically you'd want a deep copy, but let's keep it simple.
        Card clonedCard = Card.builder().build();
        clonedCard.setExamples(examples.stream()
                .map(e -> new Example(e.getId(), e.getExample(), e.getTranslation(), clonedCard))
                .collect(Collectors.toList()));
        clonedCard.setTranslations(translations.stream()
                .map(t -> new Translation(t.getId(), t.getTranslation(), clonedCard))
                .collect(Collectors.toList()));

        when(cardMapper.copyCardDtoToEntity(eq(originalCardDto))).thenReturn(clonedCard);
        when(learnerFinder.findLearner(actionExecutor, originalCard.getLanguage()))
                .thenReturn(recipientLearner);
        // The user is authorized
        // Act
        cardSuggestionService.acceptSuggestion(suggestionId, actionExecutor);

        // Assert
        verify(cardRepository)
                .save(argThat(savedCard -> savedCard.getExamples().size() == examples.size()
                        && savedCard.getTranslations().size() == translations.size()));
        verify(translationRepository).saveAll(clonedCard.getTranslations());
        verify(exampleRepository).saveAll(clonedCard.getExamples());
        verify(learnerRepository).save(eq(recipientLearner));
        verify(cardSuggestionRepository).delete(eq(cardSuggestion));
    }

    @DisplayName("Should save a card suggestion and return true if it doesn't exist")
    @Test
    void givenNewCardSuggestionDtoAndUser_whenSuggestCard_thenReturnTrueAndSaveIt() {
        // Arrange
        // The new code expects (CardSuggestionDto dto, User user)
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        User user = User.builder().id(101L).build();

        // Suppose the card has a language = EN, so we find the Learner for that user+lang
        Card card = Card.builder()
                .id(2L)
                .examples(new ArrayList<>())
                .translations(new ArrayList<>())
                .cardTags(Set.of())
                .language(Language.EN)
                .build();

        // The findById for the card
        when(cardRepository.findById(dto.cardId())).thenReturn(Optional.of(card));

        // We mock the Learner returned for user & card.getLanguage()
        Learner senderLearner = Learner.builder().id(300L).build();
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(senderLearner);

        // The recipient learner from the DB
        Learner recipient = Learner.builder().id(dto.recipientId()).build();
        when(learnerRepository.findById(dto.recipientId())).thenReturn(Optional.of(recipient));

        // If there's no existing suggestion, the repository returns null
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(senderLearner, recipient, card))
                .thenReturn(null);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, user);

        // Assert
        assertThat(result).isTrue();
        verify(cardSuggestionRepository)
                .save(argThat(suggestion -> suggestion.getSender().equals(senderLearner)
                        && suggestion.getRecipient().equals(recipient)
                        && suggestion.getCard().equals(card)));
    }

    @DisplayName("Should not save a card suggestion and return false if it already exists")
    @Test
    void givenExistingCardSuggestionDtoAndUser_whenSuggestCard_thenReturnFalseAndDoNotSaveIt() {
        // Arrange
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        User user = User.builder().id(202L).build();

        Card card = Card.builder().id(1L).language(Language.EN).build();
        when(cardRepository.findById(dto.cardId())).thenReturn(Optional.of(card));

        // The learner corresponding to user+language
        Learner senderLearner = Learner.builder().id(333L).build();
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(senderLearner);

        Learner recipient = Learner.builder().id(dto.recipientId()).build();
        when(learnerRepository.findById(dto.recipientId())).thenReturn(Optional.of(recipient));

        // Existing suggestion
        CardSuggestion existingSuggestion = new CardSuggestion(senderLearner, recipient, card);
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(senderLearner, recipient, card))
                .thenReturn(existingSuggestion);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, user);

        // Assert
        assertThat(result).isFalse();
        verify(cardSuggestionRepository, never()).save(any(CardSuggestion.class));
    }

    @DisplayName("Should throw EntityNotFoundException when CardSuggestion not found")
    @Test
    void givenNonExistingCardSuggestionId_whenAcceptSuggestion_thenThrowEntityNotFoundException() {
        // Arrange
        Long nonExistingId = 99L;
        User user = User.builder().id(42L).build();

        when(cardSuggestionRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cardSuggestionService.acceptSuggestion(nonExistingId, user))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Card suggestion with ID " + nonExistingId + " not found");
    }
}

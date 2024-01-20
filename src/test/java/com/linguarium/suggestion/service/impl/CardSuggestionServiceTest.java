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
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    UserRepository userRepository;
    @Mock
    CardMapper cardMapper;

    @InjectMocks
    CardSuggestionServiceImpl cardSuggestionService;

    @BeforeEach
    void setUp() {
        cardSuggestionService = new CardSuggestionServiceImpl(cardRepository, cardSuggestionRepository, exampleRepository, translationRepository, userRepository, cardMapper);
    }

    @Test
    @DisplayName("Should return list of suggested CardDto")
    public void givenUser_whenGetSuggestedCards_thenReturnListOfCardDto() {
        // Arrange
        User user = new User();
        Long id = 1L;
        User sender = User.builder().id(id).build();
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

    @Test
    @DisplayName("Should delete the suggestion")
    public void givenCardAcceptanceDtoAndUser_whenDeclineSuggestion_thenSuggestionDeleted() {
        // Arrange
        //TODO provoke illegalAccess
        Long suggestionId = 3L;
        Long userId = 4L;

        User recipient = User.builder().id(userId).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(Card.builder().build())
                .recipient(recipient)
                .build();
        when(cardSuggestionRepository.getById(suggestionId)).thenReturn(cardSuggestion);

        // Act
        cardSuggestionService.declineSuggestion(suggestionId, recipient);

        // Assert
        verify(cardSuggestionRepository).deleteById(suggestionId);
    }

    @Test
    @DisplayName("Should accept a card suggestion and clone the card")
    public void givenCardAcceptanceDtoAndRecipient_whenAcceptSuggestion_thenCloneCardAndDeleteSuggestion() {
        // Arrange
        Long suggestionId = 3L;
        Long userId = 4L;
        Card card = Card.builder().id(1L).build();
        User recipient = User.builder().id(userId).build();
        CardSuggestion cardSuggestion = CardSuggestion.builder()
                .id(suggestionId)
                .card(card)
                .recipient(recipient)
                .build();
        when(cardSuggestionRepository.getById(suggestionId)).thenReturn(cardSuggestion);

        // Create a spy of your service
        CardSuggestionServiceImpl cardServiceSpy = spy(cardSuggestionService);

        // Mock the cloneCard method to do nothing
        doNothing().when(cardServiceSpy).cloneCard(any(Card.class), any(User.class));

        // Act
        cardServiceSpy.acceptSuggestion(suggestionId, recipient);

        // Assert
        verify(cardServiceSpy).cloneCard(card, recipient);
        verify(cardSuggestionRepository).delete(cardSuggestion);
    }

    @Test
    @DisplayName("Should clone a card and save it along with its examples, translations, and tags")
    public void givenCardAndUser_whenCloneCard_thenSaveClonedCardAndRelatedEntities() {
        // Arrange
        Card card = new Card();
        User user = User.builder()
                .learner(Learner.builder()
                        .cards(new HashSet<>())
                        .build())
                .build();

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


        CardDto cardDto = new CardDto(); // Initialize as needed
        when(cardMapper.cardEntityToDto(card)).thenReturn(cardDto);
        when(cardMapper.copyCardDtoToEntity(eq(cardDto))).thenReturn(card);

        // Act
        cardSuggestionService.cloneCard(card, user);

        // Assert
        verify(cardRepository).save(argThat(savedCard ->
                savedCard.getExamples().equals(examples) &&
                        savedCard.getTranslations().equals(translations)
        ));
        verify(translationRepository).saveAll(eq(translations));
        verify(exampleRepository).saveAll(eq(examples));
        verify(userRepository).save(eq(user));
    }

    @Test
    @DisplayName("Should save a card suggestion and return true if it doesn't exist")
    public void givenNewCardSuggestionDtoAndSender_whenSuggestCard_thenReturnTrueAndSaveIt() {
        // Arrange
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        User sender = new User();
        User recipient = new User();
        Card card = Card.builder().id(2L)
                .examples(new ArrayList<>())
                .translations(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.getById(dto.getCardId())).thenReturn(card);
        when(userRepository.getById(dto.getRecipientId())).thenReturn(recipient);
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card)).thenReturn(null);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, sender);

        // Assert
        assertThat(result).isTrue();
        verify(cardSuggestionRepository).save(argThat(suggestion ->
                suggestion.getSender().equals(sender) &&
                        suggestion.getRecipient().equals(recipient) &&
                        suggestion.getCard().equals(card)
        ));
    }

    @Test
    @DisplayName("Should not save a card suggestion and return false if it already exists")
    public void givenExistingCardSuggestionDtoAndSender_whenSuggestCard_thenReturnFalseAndDoNotSaveIt() {
        // Arrange
        CardSuggestionDto dto = new CardSuggestionDto(1L, 2L);
        User sender = new User();
        User recipient = new User();
        Card card = new Card();
        CardSuggestion existingSuggestion = new CardSuggestion(sender, recipient, card);

        when(cardRepository.getById(dto.getCardId())).thenReturn(card);
        when(userRepository.getById(dto.getRecipientId())).thenReturn(recipient);
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card))
                .thenReturn(existingSuggestion);

        // Act
        boolean result = cardSuggestionService.suggestCard(dto, sender);

        // Assert
        assertThat(result).isFalse();
        verify(cardSuggestionRepository, never()).save(any(CardSuggestion.class));
    }
}
package com.linguatool.service;

import com.linguatool.model.dto.CardAcceptanceDto;
import com.linguatool.model.dto.CardSuggestionDto;
import com.linguatool.model.dto.external_api.request.*;
import com.linguatool.model.entity.lang.*;
import com.linguatool.model.entity.user.User;
import com.linguatool.model.mapping.CardMapper;
import com.linguatool.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CardServiceTest {
    @Mock
    CardRepository cardRepository;
    @Mock
    CardSuggestionRepository cardSuggestionRepository;
    @Mock
    CardTagRepository cardTagRepository;
    @Mock
    TagRepository tagRepository;
    @Mock
    ExampleRepository exampleRepository;
    @Mock
    TranslationRepository translationRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    LanguageRepository languageRepository;
    @Mock
    CardMapper cardMapper;

    @InjectMocks
    CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardService = new CardService(cardRepository, cardSuggestionRepository, cardTagRepository, tagRepository, exampleRepository, translationRepository, userRepository, languageRepository, cardMapper);
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
        List<CardDto> result = cardService.getSuggestedCards(user);

        // Assert
        assertThat(result).containsExactly(expectedDto);
    }

    @Test
    @DisplayName("Should delete the suggestion")
    public void givenCardAcceptanceDtoAndUser_whenDeclineSuggestion_thenSuggestionDeleted() {
        // Arrange
        Long senderId = 1L;
        Long cardId = 2L;
        Long recipientId = 3L;
        CardAcceptanceDto dto = new CardAcceptanceDto(senderId, cardId);
        User recipient = User.builder().id(recipientId).build();

        // Act
        cardService.declineSuggestion(dto, recipient);

        // Assert
        verify(cardSuggestionRepository).deleteBySenderIdAndRecipientIdAndCardId(senderId, recipientId, cardId);
    }

    @Test
    @DisplayName("Should accept a card suggestion and clone the card")
    public void givenCardAcceptanceDtoAndRecipient_whenAcceptSuggestion_thenCloneCardAndDeleteSuggestion() {
        // Arrange
        CardAcceptanceDto dto = new CardAcceptanceDto(1L, 2L);
        User recipient = new User();
        User sender = new User();
        Card card = new Card();
        CardSuggestion cardSuggestion = new CardSuggestion(sender, recipient, card);

        when(userRepository.getById(dto.getSenderId())).thenReturn(sender);
        when(cardRepository.getById(dto.getCardId())).thenReturn(card);
        when(cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card)).thenReturn(cardSuggestion);

        // Create a spy of your service
        CardService cardServiceSpy = spy(cardService);

        // Mock the cloneCard method to do nothing
        doNothing().when(cardServiceSpy).cloneCard(any(Card.class), any(User.class));

        // Act
        cardServiceSpy.acceptSuggestion(dto, recipient);

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
                .cards(new HashSet<>())
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
        when(cardMapper.copyCardDtoToEntity(eq(cardDto), any(LanguageRepository.class))).thenReturn(card);

        // Act
        cardService.cloneCard(card, user);

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
        boolean result = cardService.suggestCard(dto, sender);

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
        boolean result = cardService.suggestCard(dto, sender);

        // Assert
        assertThat(result).isFalse();
        verify(cardSuggestionRepository, never()).save(any(CardSuggestion.class));
    }

    @Test
    @DisplayName("Should return a list of CardDto that match the search entry")
    public void givenSearchEntryAndUser_whenSearchByEntry_thenReturnMatchingCards() {
        // Arrange
        String entry = "test";
        User user = new User();
        Card card1 = Card.builder().id(1L).build();
        card1.setEntry("test1");
        Card card2 = Card.builder().id(2L).build();
        card2.setEntry("test2");
        List<Card> cards = Arrays.asList(card1, card2);

        when(cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(user, "%test%")).thenReturn(cards);
        when(cardMapper.cardEntityToDto(card1)).thenReturn(new CardDto());
        when(cardMapper.cardEntityToDto(card2)).thenReturn(new CardDto());

        // Act
        List<CardDto> result = cardService.searchByEntry(entry, user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should return CardDto when getCardById is called")
    public void givenCardId_whenGetCardById_thenReturnCardDto() {
        // Arrange
        Long id = 1L;
        Card card = Card.builder().id(id).build();
        CardDto expectedDto = CardDto.builder()
                .id(1L)
                .build();

        when(cardRepository.getById(id)).thenReturn(card);
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardService.getCardById(id);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Should return CardDto when getCardByHash is called")
    public void givenCardHash_whenGetCardByHash_thenReturnCardDto() {
        // Arrange
        String hash = "someHash";
        Card card = Card.builder().hash(hash).build();
        CardDto expectedDto = new CardDto();

        when(cardRepository.getByHash(hash)).thenReturn(Optional.of(card));
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardService.getCardByHash(hash);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Should return list of CardDto when getUsersCards is called")
    public void givenUser_whenGetUsersCards_thenReturnListOfCardDto() {
        // Arrange
        User user = new User();
        Card card1 = Card.builder().id(1L).build();
        Card card2 = Card.builder().id(2L).build();
        List<Card> cards = Arrays.asList(card1, card2);

        CardDto dto1 = new CardDto();
        CardDto dto2 = new CardDto();
        List<CardDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(cardRepository.findAllByOwner(user)).thenReturn(cards);
        when(cardMapper.cardEntityToDto(card1)).thenReturn(dto1);
        when(cardMapper.cardEntityToDto(card2)).thenReturn(dto2);

        // Act
        List<CardDto> result = cardService.getUsersCards(user);

        // Assert
        assertThat(result).isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("Should throw exception when getCardByHash is called with non-existent hash")
    public void givenNonExistentCardHash_whenGetCardByHash_thenThrowException() {
        // Arrange
        String hash = "nonExistentHash";

        when(cardRepository.getByHash(hash)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> cardService.getCardByHash(hash));
    }

    @Test
    @DisplayName("Should delete specified examples")
    public void givenCardUpdateDto_whenUpdateCard_thenExamplesDeleted() {
        // Arrange
        Long cardId = 1L;
        int[] ex_del = {4, 5};  // IDs of examples to be deleted

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .ex_del(ex_del)
                .tr_del(new int[]{})
                .tags(new TagDto[]{})
                .translations(new TranslationDto[]{})
                .examples(new ExampleDto[]{})
                .build();

        User user = new User();

        // Create a list of examples, some of which will be deleted
        List<Example> examples = new ArrayList<>();
        examples.add(Example.builder().id(4L).example("example1").translation("translation1").build());
        examples.add(Example.builder().id(5L).example("example2").translation("translation2").build());
        examples.add(Example.builder().id(6L).example("example3").translation("translation3").build());

        Card card = Card.builder().id(cardId)
                .examples(examples)
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify deletion of examples
        for (int id : ex_del) {
            verify(exampleRepository).deleteById((long) id);
        }
    }

    @Test
    @DisplayName("Should delete specified translations")
    public void givenCardUpdateDto_whenUpdateCard_thenTranslationsDeleted() {
        // Arrange
        Long cardId = 1L;
        int[] tr_del = {2, 3};  // IDs of translations to be deleted

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .tr_del(tr_del)
                .ex_del(new int[]{})
                .tags(new TagDto[]{})
                .translations(new TranslationDto[]{})
                .examples(new ExampleDto[]{})
                .build();

        User user = new User();

        // Create a list of translations, some of which will be deleted
        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().id(1L).translation("trans1").build());
        translations.add(Translation.builder().id(2L).translation("trans2").build());
        translations.add(Translation.builder().id(3L).translation("trans3").build());

        Card card = Card.builder().id(cardId)
                .translations(translations)
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify deletion of translations
        for (int id : tr_del) {
            verify(translationRepository).deleteById((long) id);
        }
    }

    @Test
    @DisplayName("Should update existing translations")
    public void givenCardUpdateDto_whenUpdateCard_thenExistingTranslationsUpdated() {
        // Arrange
        Long cardId = 1L;

        TranslationDto[] updatedTranslations = {
                new TranslationDto(1L, "updatedTranslation1"),
                new TranslationDto(2L, "updatedTranslation2")
        };

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(updatedTranslations)
                .examples(new ExampleDto[]{})
                .tags(new TagDto[]{})
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();

        // Create a list of translations, some of which will be updated
        List<Translation> originalTranslations = new ArrayList<>();
        originalTranslations.add(Translation.builder().id(1L).translation("originalTranslation1").build());
        originalTranslations.add(Translation.builder().id(2L).translation("originalTranslation2").build());

        Card card = Card.builder().id(cardId)
                .translations(originalTranslations)
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Mocking translation retrieval
        for (TranslationDto translationDto : updatedTranslations) {
            when(translationRepository.getById(translationDto.getId())).thenReturn(
                    Translation.builder()
                            .id(translationDto.getId())
                            .translation(translationDto.getTranslation())
                            .build());
        }

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify update of translations
        for (TranslationDto updatedTranslation : updatedTranslations) {
            verify(translationRepository).save(argThat(translation ->
                    translation.getId().equals(updatedTranslation.getId()) &&
                            translation.getTranslation().equals(updatedTranslation.getTranslation())));
        }
    }

    @Test
    @DisplayName("Should update existing examples")
    public void givenCardUpdateDto_whenUpdateCard_thenExistingExamplesUpdated() {
        // Arrange
        Long cardId = 1L;

        ExampleDto[] updatedExamples = {
                new ExampleDto(1L, "updatedExample1", "updatedTranslation1"),
                new ExampleDto(2L, "updatedExample2", "updatedTranslation2")
        };

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[]{})
                .examples(updatedExamples)
                .tags(new TagDto[]{})
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();

        // Create a list of examples, some of which will be updated
        List<Example> originalExamples = new ArrayList<>();
        originalExamples.add(Example.builder().id(1L).example("originalExample1").translation("originalTranslation1").build());
        originalExamples.add(Example.builder().id(2L).example("originalExample2").translation("originalTranslation2").build());

        Card card = Card.builder().id(cardId)
                .examples(originalExamples)
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Mocking example retrieval
        for (ExampleDto exampleDto : updatedExamples) {
            when(exampleRepository.getById(exampleDto.getId())).thenReturn(
                    Example.builder()
                            .id(exampleDto.getId())
                            .example(exampleDto.getExample())
                            .translation(exampleDto.getTranslation())
                            .build());
        }

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify update of examples
        for (ExampleDto updatedExample : updatedExamples) {
            verify(exampleRepository).save(argThat(example ->
                    example.getId().equals(updatedExample.getId()) &&
                            example.getExample().equals(updatedExample.getExample()) &&
                            example.getTranslation().equals(updatedExample.getTranslation())));
        }
    }

    @Test
    @DisplayName("Should create new translations")
    public void givenCardUpdateDto_whenUpdateCard_thenNewTranslationsCreated() {
        // Arrange
        Long cardId = 1L;

        TranslationDto[] newTranslations = {
                new TranslationDto(null, "newTranslation1"),
                new TranslationDto(null, "newTranslation2")
        };

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(newTranslations)
                .examples(new ExampleDto[]{})
                .tags(new TagDto[]{})
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();

        Card card = Card.builder().id(cardId)
                .translations(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify creation of new translations
        for (TranslationDto newTranslation : newTranslations) {
            verify(translationRepository).save(argThat(translation ->
                    translation.getTranslation().equals(newTranslation.getTranslation()) &&
                            translation.getCard().equals(card)));
        }
    }

    @Test
    @DisplayName("Should create new examples")
    public void givenCardUpdateDto_whenUpdateCard_thenNewExamplesCreated() {
        // Arrange
        Long cardId = 1L;

        ExampleDto[] newExamples = {
                new ExampleDto(null, "newExample1", "newTranslation1"),
                new ExampleDto(null, "newExample2", "newTranslation2")
        };

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[]{})
                .examples(newExamples)
                .tags(new TagDto[]{})
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();

        Card card = Card.builder().id(cardId)
                .examples(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify creation of new examples
        for (ExampleDto newExample : newExamples) {
            verify(exampleRepository).save(argThat(example ->
                    example.getExample().equals(newExample.getExample()) &&
                            example.getTranslation().equals(newExample.getTranslation()) &&
                            example.getCard().equals(card)));
        }
    }

    @Test
    @DisplayName("Should update the timestamp of the card")
    public void givenCardUpdateDto_whenUpdateCard_thenTimestampUpdated() {
        // Arrange
        Long cardId = 1L;
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[]{})
                .examples(new ExampleDto[]{})
                .tags(new TagDto[]{})
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify that the updated timestamp was set
        assertThat(card.getUpdated()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Should save the updated card")
    public void givenCardUpdateDto_whenUpdateCard_thenCardSaved() {
        // Arrange
        Long cardId = 1L;
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[]{})
                .examples(new ExampleDto[]{})
                .tags(new TagDto[]{})
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify that the updated card was saved
        verify(cardRepository).save(card);
    }

    @Test
    @DisplayName("Should update tags for a given card")
    public void givenCardUpdateDto_whenUpdateCard_thenTagsUpdated() {
        // Arrange
        Long cardId = 1L;
        TagDto[] newTags = {new TagDto("tag1"), new TagDto("tag2")};
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[]{})
                .examples(new ExampleDto[]{})
                .tags(newTags)
                .tr_del(new int[]{})
                .ex_del(new int[]{})
                .build();

        User user = new User();
        Card card = Card.builder().id(cardId).build();

        // Mock existing tags on the card
        CardTag tag3 = CardTag.builder().tag(new Tag("tag3")).card(card).user(user).build();
        CardTag tag4 = CardTag.builder().tag(new Tag("tag4")).card(card).user(user).build();
        CardTag tag1 = CardTag.builder().tag(new Tag("tag1")).card(card).user(user).build(); // This tag should persist

        Set<CardTag> existingTags = new HashSet<>(Arrays.asList(tag3, tag4, tag1));
        card.setCardTags(existingTags);

        when(cardRepository.getById(cardId)).thenReturn(card);
        when(cardTagRepository.getByCardAndText(eq(card), eq("tag3"))).thenReturn(tag3);
        when(cardTagRepository.getByCardAndText(eq(card), eq("tag4"))).thenReturn(tag4);

        // Act
        cardService.updateCard(dto, user);

        // Assert
        // Verify deletion of old tags
        verify(cardTagRepository).delete(eq(tag3));
        verify(cardTagRepository).delete(eq(tag4));

        // Verify no deletion for the tag that should persist
        verify(cardTagRepository, never()).delete(eq(tag1));

        // Verify addition of new tags
        ArgumentCaptor<CardTag> argumentCaptor = ArgumentCaptor.forClass(CardTag.class);
        verify(cardTagRepository, times(1)).save(argumentCaptor.capture());

        List<CardTag> capturedTags = argumentCaptor.getAllValues();
        assertThat(capturedTags).extracting(CardTag::getTag)
                .extracting(Tag::getText)
                .containsExactlyInAnyOrder("tag2"); // tag1 should not be added again
    }

    @Test
    @DisplayName("Should throw NoSuchElement when given bad language code")
    void givenUserAndNonExistentLanguageCode_whenGetUsersCardsOfLang_thenThrowException() {
        when(languageRepository.findByCode(eq(Language.GERMAN))).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> cardService.getUsersCardsOfLang(Language.GERMAN.getCode(), new User()));

    }

    @Test
    @DisplayName("Should return user's cards of the specified language")
    void givenUserAndLanguageCode_whenGetUsersCardsOfLang_thenReturnRightCards() {
        // Mocked data
        User user = new User();

        LanguageEntity languageEntity = new LanguageEntity(1L, Language.GERMAN);
        when(languageRepository.findByCode(eq(Language.GERMAN))).thenReturn(Optional.of(languageEntity));

        List<Card> mockedCards = List.of(
                Card.builder()
                        .id(1L)
                        .build(),
                Card.builder()
                        .id(2L)
                        .build(),
                Card.builder()
                        .id(3L)
                        .build()
        );
        when(cardRepository.findAllByOwnerAndLanguage(user, languageEntity)).thenReturn(mockedCards);

        List<CardDto> mockedCardDtos = List.of(
                CardDto.builder()
                        .id(1L)
                        .build(),
                CardDto.builder()
                        .id(2L)
                        .build(),
                CardDto.builder()
                        .id(3L)
                        .build()
        );
        for (int i = 0; i < mockedCards.size(); i++) {
            when(cardMapper.cardEntityToDto(eq(mockedCards.get(i)))).thenReturn(mockedCardDtos.get(i));
        }

        // Invoke the method
        List<CardDto> result = cardService.getUsersCardsOfLang(Language.GERMAN.getCode(), user);

        // Assertions
        assertThat(result).hasSize(mockedCardDtos.size()).containsExactlyElementsOf(mockedCardDtos);
    }
}
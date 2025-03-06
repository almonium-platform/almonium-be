package com.almonium.card.core.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.ExampleDto;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.TranslationDto;
import com.almonium.card.core.dto.request.CardCreationDto;
import com.almonium.card.core.dto.request.CardUpdateDto;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.mapper.CardMapper;
import com.almonium.card.core.model.entity.Card;
import com.almonium.card.core.model.entity.CardTag;
import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.Tag;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.model.entity.pk.CardTagPK;
import com.almonium.card.core.repository.CardRepository;
import com.almonium.card.core.repository.CardTagRepository;
import com.almonium.card.core.repository.ExampleRepository;
import com.almonium.card.core.repository.TagRepository;
import com.almonium.card.core.repository.TranslationRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.almonium.util.TestDataGenerator;
import com.google.common.collect.Sets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Refactored test class for the new CardService method signatures
 */
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class CardServiceTest {

    @Mock
    CardRepository cardRepository;

    @Mock
    CardTagRepository cardTagRepository;

    @Mock
    TagRepository tagRepository;

    @Mock
    ExampleRepository exampleRepository;

    @Mock
    TranslationRepository translationRepository;

    @Mock
    LearnerRepository learnerRepository;

    @Mock
    CardMapper cardMapper;
    /**
     * NEW: The service now depends on a LearnerFinder to get Learner from (User, Language).
     */
    @Mock
    LearnerFinder learnerFinder;

    @InjectMocks
    CardService cardService;

    @Captor
    ArgumentCaptor<List<CardTag>> captor;

    @DisplayName("Should return a list of CardDto that match the search entry")
    @Test
    void givenSearchEntryAndUser_whenSearchByEntry_thenReturnMatchingCards() {
        // Arrange
        Language language = Language.EN;

        User user = User.builder().id(1L).build();
        Learner learner = Learner.builder().id(1L).language(language).build();

        // The new CardService calls: learnerFinder.findLearner(user, language)
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Card card1 = Card.builder().id(1L).entry("test1").build();
        Card card2 = Card.builder().id(2L).entry("test2").build();
        List<Card> cards = Arrays.asList(card1, card2);
        String entry = "test";

        when(cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(learner, "%test%"))
                .thenReturn(cards);

        when(cardMapper.cardEntityToDto(card1))
                .thenReturn(CardDto.builder().id(1L).build());
        when(cardMapper.cardEntityToDto(card2))
                .thenReturn(CardDto.builder().id(2L).build());

        // Act
        List<CardDto> result = cardService.searchByEntry(entry, language, user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @DisplayName("Should return CardDto when getCardById is called")
    @Test
    void givenCardId_whenGetCardById_thenReturnCardDto() {
        // Arrange
        UUID id = 1L;
        Card card = Card.builder().id(id).build();
        CardDto expectedDto = CardDto.builder().id(1L).build();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardService.getCardById(id);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @DisplayName("Should return CardDto when getCardByHash is called")
    @Test
    void givenCardHash_whenGetCardByHash_thenReturnCardDto() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Card card = Card.builder().publicId(uuid).build();
        CardDto expectedDto = CardDto.builder().id(999L).build();

        when(cardRepository.getByPublicId(uuid)).thenReturn(Optional.of(card));
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardService.getCardByPublicId(uuid.toString());

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @DisplayName("Should return list of CardDto (for a given user+language) when getUsersCardsOfLang is called")
    @Test
    void givenUserAndLanguage_whenGetUsersCardsOfLang_thenReturnListOfCardDto() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(100L).build();
        Learner learner = Learner.builder().id(200L).build();

        // The service calls: learnerFinder.findLearner(user, language)
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Card card1 = Card.builder().id(1L).build();
        Card card2 = Card.builder().id(2L).build();
        List<Card> cards = Arrays.asList(card1, card2);

        CardDto dto1 = CardDto.builder().id(1L).build();
        CardDto dto2 = CardDto.builder().id(2L).build();
        List<CardDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(cardRepository.findAllByOwner(learner)).thenReturn(cards);
        when(cardMapper.cardEntityToDto(card1)).thenReturn(dto1);
        when(cardMapper.cardEntityToDto(card2)).thenReturn(dto2);

        // Act
        List<CardDto> result = cardService.getUsersCardsOfLang(user, language);

        // Assert
        assertThat(result).isEqualTo(expectedDtos);
    }

    @DisplayName("Should throw exception when getCardByHash is called with non-existent hash")
    @Test
    void givenNonExistentCardHash_whenGetCardByHash_thenThrowException() {
        // Arrange
        UUID random = UUID.randomUUID();

        when(cardRepository.getByPublicId(random)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> cardService.getCardByPublicId(random.toString()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("Should delete card by ID")
    @Test
    void givenCardId_whenDeleteById_thenCardIsDeleted() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        cardService.deleteById(id);

        // Assert
        verify(cardRepository).deleteById(id);
    }

    @DisplayName("Should delete specified examples")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenExamplesDeleted() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(777L).build();
        Learner learner = Learner.builder().id(888L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        int[] deletedExamplesIds = {4, 5}; // IDs of examples to be deleted

        List<Example> examples = new ArrayList<>();
        examples.add(Example.builder()
                .id(4L)
                .example("example1")
                .translation("translation1")
                .build());
        examples.add(Example.builder()
                .id(5L)
                .example("example2")
                .translation("translation2")
                .build());
        examples.add(Example.builder()
                .id(6L)
                .example("example3")
                .translation("translation3")
                .build());

        Card card =
                Card.builder().id(cardId).examples(examples).cardTags(Set.of()).build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .deletedExamplesIds(deletedExamplesIds)
                .deletedTranslationsIds(new int[] {})
                .tags(new TagDto[] {})
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (int id : deletedExamplesIds) {
            verify(exampleRepository).deleteById((long) id);
        }
    }

    @DisplayName("Should delete specified translations")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTranslationsDeleted() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(2L).build();
        Learner learner = Learner.builder().id(99L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        int[] deletedTranslationsIds = {2, 3};

        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().id(1L).translation("trans1").build());
        translations.add(Translation.builder().id(2L).translation("trans2").build());
        translations.add(Translation.builder().id(3L).translation("trans3").build());

        Card card = Card.builder()
                .id(cardId)
                .translations(translations)
                .cardTags(Set.of())
                .build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .deletedTranslationsIds(deletedTranslationsIds)
                .deletedExamplesIds(new int[] {})
                .tags(new TagDto[] {})
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (int id : deletedTranslationsIds) {
            verify(translationRepository).deleteById((long) id);
        }
    }

    @DisplayName("Should update existing translations")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenExistingTranslationsUpdated() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(11L).build();
        Learner learner = Learner.builder().id(12L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        TranslationDto[] updatedTranslations = {
            new TranslationDto(1L, "updatedTranslation1"), new TranslationDto(2L, "updatedTranslation2")
        };

        List<Translation> originalTranslations = new ArrayList<>();
        originalTranslations.add(
                Translation.builder().id(1L).translation("originalTranslation1").build());
        originalTranslations.add(
                Translation.builder().id(2L).translation("originalTranslation2").build());

        Card card = Card.builder()
                .id(cardId)
                .translations(originalTranslations)
                .cardTags(Set.of())
                .build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .translations(updatedTranslations)
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking the DB fetch of each translation
        for (TranslationDto t : updatedTranslations) {
            when(translationRepository.findById(t.getId()))
                    .thenReturn(Optional.of(Translation.builder()
                            .id(t.getId())
                            .translation(t.getTranslation())
                            .build()));
        }

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (TranslationDto updatedTranslation : updatedTranslations) {
            verify(translationRepository)
                    .save(argThat(translation -> translation.getId().equals(updatedTranslation.getId())
                            && translation.getTranslation().equals(updatedTranslation.getTranslation())));
        }
    }

    @DisplayName("Should update existing examples")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenExistingExamplesUpdated() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(22L).build();
        Learner learner = Learner.builder().id(33L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        ExampleDto[] updatedExamples = {
            new ExampleDto(1L, "updatedExample1", "updatedTranslation1"),
            new ExampleDto(2L, "updatedExample2", "updatedTranslation2")
        };

        List<Example> originalExamples = new ArrayList<>();
        originalExamples.add(Example.builder()
                .id(1L)
                .example("originalExample1")
                .translation("originalTranslation1")
                .build());
        originalExamples.add(Example.builder()
                .id(2L)
                .example("originalExample2")
                .translation("originalTranslation2")
                .build());

        Card card = Card.builder()
                .id(cardId)
                .examples(originalExamples)
                .cardTags(Set.of())
                .build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .examples(updatedExamples)
                .translations(new TranslationDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking DB fetch
        for (ExampleDto e : updatedExamples) {
            when(exampleRepository.findById(e.getId()))
                    .thenReturn(Optional.of(Example.builder()
                            .id(e.getId())
                            .example(e.getExample())
                            .translation(e.getTranslation())
                            .build()));
        }

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (ExampleDto updatedExample : updatedExamples) {
            verify(exampleRepository)
                    .save(argThat(example -> example.getId().equals(updatedExample.getId())
                            && example.getExample().equals(updatedExample.getExample())
                            && example.getTranslation().equals(updatedExample.getTranslation())));
        }
    }

    @DisplayName("Should create new translations")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenNewTranslationsCreated() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(45L).build();
        Learner learner = Learner.builder().id(46L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        TranslationDto[] newTranslations = {
            new TranslationDto(null, "newTranslation1"), new TranslationDto(null, "newTranslation2")
        };

        Card card = Card.builder()
                .id(cardId)
                .translations(new ArrayList<>())
                .cardTags(Set.of())
                .build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .translations(newTranslations)
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (TranslationDto t : newTranslations) {
            verify(translationRepository)
                    .save(argThat(tr -> tr.getTranslation().equals(t.getTranslation())
                            && tr.getCard().equals(card)));
        }
    }

    @DisplayName("Should create new examples")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenNewExamplesCreated() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(55L).build();
        Learner learner = Learner.builder().id(56L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        ExampleDto[] newExamples = {
            new ExampleDto(null, "newExample1", "newTranslation1"),
            new ExampleDto(null, "newExample2", "newTranslation2")
        };

        Card card = Card.builder()
                .id(cardId)
                .examples(new ArrayList<>())
                .cardTags(Set.of())
                .build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .examples(newExamples)
                .translations(new TranslationDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (ExampleDto e : newExamples) {
            verify(exampleRepository)
                    .save(argThat(ex -> ex.getExample().equals(e.getExample())
                            && ex.getTranslation().equals(e.getTranslation())
                            && ex.getCard().equals(card)));
        }
    }

    @DisplayName("Should update the timestamp of the card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTimestampUpdated() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(66L).build();
        Learner learner = Learner.builder().id(67L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        assertThat(card.getUpdatedAt()).isCloseTo(Instant.now(), within(2, ChronoUnit.SECONDS));
    }

    @DisplayName("Should save the updated card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenCardSaved() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(77L).build();
        Learner learner = Learner.builder().id(78L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 1L;
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        verify(cardRepository).save(card);
    }

    @DisplayName("Should update tags for a given card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTagsUpdated() {
        // Arrange
        Language language = Language.EN;
        User user = User.builder().id(88L).build();
        Learner learner = Learner.builder().id(99L).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        Long cardId = 7L;
        String tagToBeDeleted = "tagtobedeleted";
        String oldTag1 = "oldtag1";
        String oldTag2 = "oldtag2";
        String newTag = "newtag";

        CardUpdateDto dto = createCardUpdateDto(cardId, language, oldTag1, oldTag2, newTag);

        Card card = Card.builder().id(cardId).build();

        CardTag cardTagToBeDeleted = createCardTag(card, learner, tagToBeDeleted);
        CardTag oldCardTag1 = createCardTag(card, learner, oldTag1);
        CardTag oldCardTag2 = createCardTag(card, learner, oldTag2);
        HashSet<CardTag> existingCardTagsSet = Sets.newHashSet(cardTagToBeDeleted, oldCardTag1, oldCardTag2);
        card.setCardTags(existingCardTagsSet);

        mockCardTagRepository(List.of(cardTagToBeDeleted));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        verify(cardTagRepository).delete(cardTagToBeDeleted);
        verify(cardTagRepository, never()).delete(oldCardTag1);
        verify(cardTagRepository, never()).delete(oldCardTag2);

        // Assert that we saved the new tag
        ArgumentCaptor<CardTag> argumentCaptor = ArgumentCaptor.forClass(CardTag.class);
        verify(cardTagRepository, times(1)).save(argumentCaptor.capture());

        List<CardTag> capturedTags = argumentCaptor.getAllValues();
        assertThat(capturedTags)
                .extracting(CardTag::getTag)
                .extracting(Tag::getText)
                .containsExactlyInAnyOrder(newTag);
    }

    @DisplayName("Should create a new card with all associated entities")
    @Test
    void givenCardCreationDtoAndUser_whenCreateCard_thenCardCreatedWithAllEntities() {
        // Arrange
        User user = mock(User.class);

        CardCreationDto mockDto = mock(CardCreationDto.class);
        Card mockCard = mock(Card.class);
        List<Example> mockExamples = Collections.singletonList(mock(Example.class));
        List<Translation> mockTranslations = Collections.singletonList(mock(Translation.class));

        TagDto[] mockTags = {
            TagDto.builder().text("text1").build(),
            TagDto.builder().text("text2").build()
        };

        // We must also specify the language, as required by the new createCard(User, CardCreationDto)
        when(mockDto.getLanguage()).thenReturn(Language.EN);

        // The service will do: learnerFinder.findLearner(user, Language.EN)
        Learner learner = Learner.builder().build();
        when(learnerFinder.findLearner(user, Language.EN)).thenReturn(learner);

        when(cardMapper.cardDtoToEntity(mockDto)).thenReturn(mockCard);

        when(tagRepository.findByTextWithNormalization(eq("text1"))).thenReturn(Optional.empty());
        when(tagRepository.findByTextWithNormalization(eq("text2")))
                .thenReturn(Optional.of(Tag.builder().id(22L).text("text2").build()));

        when(mockCard.getExamples()).thenReturn(mockExamples);
        when(mockCard.getTranslations()).thenReturn(mockTranslations);
        when(mockDto.getTags()).thenReturn(mockTags);

        // Act
        cardService.createCard(user, mockDto);

        // Assert
        verify(cardTagRepository).saveAll(captor.capture());
        List<CardTag> capturedCardTags = captor.getValue();
        assertThat(capturedCardTags.size()).isEqualTo(2);
        assertThat(capturedCardTags.get(0).getTag().getText()).isEqualTo("text1");
        assertThat(capturedCardTags.get(1).getTag().getText()).isEqualTo("text2");

        verify(tagRepository).save(eq(new Tag("text1")));
        verify(tagRepository).findByTextWithNormalization(eq("text1"));
        verify(tagRepository).findByTextWithNormalization(eq("text2"));
        verify(translationRepository).saveAll(mockTranslations);
        verify(exampleRepository).saveAll(mockExamples);
        verify(learnerRepository).save(learner);
    }

    @DisplayName("Should return user's cards of the specified language")
    @Test
    void givenUserAndLanguageCode_whenGetUsersCardsOfLang_thenReturnRightCards() {
        // Arrange
        Language testLanguage = Language.DE;
        User user = new User();
        Learner learner = new Learner();
        when(learnerFinder.findLearner(user, testLanguage)).thenReturn(learner);

        List<Card> mockedCards = List.of(
                Card.builder().id(1L).build(),
                Card.builder().id(2L).build(),
                Card.builder().id(3L).build());
        when(cardRepository.findAllByOwner(learner)).thenReturn(mockedCards);

        List<CardDto> mockedCardDtos = List.of(
                CardDto.builder().id(1L).build(),
                CardDto.builder().id(2L).build(),
                CardDto.builder().id(3L).build());
        for (int i = 0; i < mockedCards.size(); i++) {
            when(cardMapper.cardEntityToDto(eq(mockedCards.get(i)))).thenReturn(mockedCardDtos.get(i));
        }

        // Act
        List<CardDto> result = cardService.getUsersCardsOfLang(user, testLanguage);

        // Assert
        assertThat(result).hasSize(mockedCardDtos.size()).containsExactlyElementsOf(mockedCardDtos);
    }

    // -------------------------------------------------------------------------
    // Helper methods for building DTOs / mocks
    // -------------------------------------------------------------------------

    private CardUpdateDto createCardUpdateDto(Long cardId, Language language, String... tags) {
        TagDto[] tagDtos = Arrays.stream(tags).map(TagDto::new).toArray(TagDto[]::new);
        return CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(tagDtos)
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();
    }

    private CardTag createCardTag(Card card, Learner learner, String tagText) {
        return CardTag.builder()
                .id(new CardTagPK(card.getId(), TestDataGenerator.random().nextLong()))
                .card(card)
                .tag(new Tag(tagText))
                .learner(learner)
                .build();
    }

    private void mockCardTagRepository(List<CardTag> existingCardTags) {
        // We'll assume they all belong to the same card
        Card card = existingCardTags.iterator().next().getCard();
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(card));

        existingCardTags.forEach(cardTag -> when(cardTagRepository.getByCardAndText(
                        card, cardTag.getTag().getText()))
                .thenReturn(cardTag));
    }
}

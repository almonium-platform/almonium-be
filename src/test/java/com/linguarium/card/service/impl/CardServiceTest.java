package com.linguarium.card.service.impl;

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

import com.google.common.collect.Sets;
import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.card.dto.ExampleDto;
import com.linguarium.card.dto.TagDto;
import com.linguarium.card.dto.TranslationDto;
import com.linguarium.card.mapper.CardMapper;
import com.linguarium.card.model.Card;
import com.linguarium.card.model.CardTag;
import com.linguarium.card.model.CardTagPK;
import com.linguarium.card.model.Example;
import com.linguarium.card.model.Tag;
import com.linguarium.card.model.Translation;
import com.linguarium.card.repository.CardRepository;
import com.linguarium.card.repository.CardTagRepository;
import com.linguarium.card.repository.ExampleRepository;
import com.linguarium.card.repository.TagRepository;
import com.linguarium.card.repository.TranslationRepository;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import com.linguarium.util.TestDataGenerator;
import java.time.LocalDateTime;
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
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
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
    @InjectMocks
    CardServiceImpl cardServiceImpl;

    @Captor
    private ArgumentCaptor<List<CardTag>> captor;

    @DisplayName("Should return a list of CardDto that match the search entry")
    @Test
    void givenSearchEntryAndUser_whenSearchByEntry_thenReturnMatchingCards() {
        // Arrange
        Learner user = new Learner();
        Card card1 = Card.builder().id(1L).build();
        card1.setEntry("test1");
        Card card2 = Card.builder().id(2L).build();
        card2.setEntry("test2");
        List<Card> cards = Arrays.asList(card1, card2);
        String entry = "test";

        when(cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(user, "%test%"))
                .thenReturn(cards);
        when(cardMapper.cardEntityToDto(card1)).thenReturn(new CardDto());
        when(cardMapper.cardEntityToDto(card2)).thenReturn(new CardDto());

        // Act
        List<CardDto> result = cardServiceImpl.searchByEntry(entry, user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @DisplayName("Should return CardDto when getCardById is called")
    @Test
    void givenCardId_whenGetCardById_thenReturnCardDto() {
        // Arrange
        Long id = 1L;
        Card card = Card.builder().id(id).build();
        CardDto expectedDto = CardDto.builder().id(1L).build();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardServiceImpl.getCardById(id);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @DisplayName("Should return CardDto when getCardByHash is called")
    @Test
    void givenCardHash_whenGetCardByHash_thenReturnCardDto() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Card card = Card.builder().publicId(uuid).build();
        CardDto expectedDto = new CardDto();

        when(cardRepository.getByPublicId(uuid)).thenReturn(Optional.of(card));
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardServiceImpl.getCardByPublicId(uuid.toString());

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @DisplayName("Should return list of CardDto when getUsersCards is called")
    @Test
    void givenUser_whenGetUsersCards_thenReturnListOfCardDto() {
        // Arrange
        Learner user = new Learner();
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
        List<CardDto> result = cardServiceImpl.getUsersCards(user);

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
        assertThatThrownBy(() -> cardServiceImpl.getCardByPublicId(random.toString()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("Should delete card by ID")
    @Test
    void givenCardId_whenDeleteById_thenCardIsDeleted() {
        Long id = 1L;

        // Act
        cardServiceImpl.deleteById(id);

        // Assert
        verify(cardRepository, times(1)).deleteById(id);
    }

    @DisplayName("Should delete specified examples")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenExamplesDeleted() {
        // Arrange
        Long cardId = 1L;
        int[] deletedExamplesIds = {4, 5}; // IDs of examples to be deleted

        // Create a list of examples, some of which will be deleted
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
                .deletedExamplesIds(deletedExamplesIds)
                .deletedTranslationsIds(new int[] {})
                .tags(new TagDto[] {})
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .build();

        Learner user = new Learner();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify deletion of examples
        for (int id : deletedExamplesIds) {
            verify(exampleRepository).deleteById((long) id);
        }
    }

    @DisplayName("Should delete specified translations")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTranslationsDeleted() {
        // Arrange
        Long cardId = 1L;
        int[] deletedTranslationsIds = {2, 3}; // IDs of translations to be deleted

        // Create a list of translations, some of which will be deleted
        List<Translation> translations = new ArrayList<>();
        translations.add(Translation.builder().id(1L).translation("trans1").build());
        translations.add(Translation.builder().id(2L).translation("trans2").build());
        translations.add(Translation.builder().id(3L).translation("trans3").build());

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .deletedTranslationsIds(deletedTranslationsIds)
                .deletedExamplesIds(new int[] {})
                .tags(new TagDto[] {})
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .build();

        Card card = Card.builder()
                .id(cardId)
                .translations(translations)
                .cardTags(Set.of())
                .build();

        Learner user = new Learner();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify deletion of translations
        for (int id : deletedTranslationsIds) {
            verify(translationRepository).deleteById((long) id);
        }
    }

    @DisplayName("Should update existing translations")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenExistingTranslationsUpdated() {
        // Arrange
        Long cardId = 1L;

        TranslationDto[] updatedTranslations = {
            new TranslationDto(1L, "updatedTranslation1"), new TranslationDto(2L, "updatedTranslation2")
        };

        // Create a list of translations, some of which will be updated
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

        Learner user = new Learner();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(updatedTranslations)
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking translation retrieval
        for (TranslationDto translationDto : updatedTranslations) {
            when(translationRepository.findById(translationDto.getId()))
                    .thenReturn(Optional.of(Translation.builder()
                            .id(translationDto.getId())
                            .translation(translationDto.getTranslation())
                            .build()));
        }

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify update of translations
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
        Long cardId = 1L;

        ExampleDto[] updatedExamples = {
            new ExampleDto(1L, "updatedExample1", "updatedTranslation1"),
            new ExampleDto(2L, "updatedExample2", "updatedTranslation2")
        };

        // Create a list of examples, some of which will be updated
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
                .translations(new TranslationDto[] {})
                .examples(updatedExamples)
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        Learner user = new Learner();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking example retrieval
        for (ExampleDto exampleDto : updatedExamples) {
            when(exampleRepository.findById(exampleDto.getId()))
                    .thenReturn(Optional.of(Example.builder()
                            .id(exampleDto.getId())
                            .example(exampleDto.getExample())
                            .translation(exampleDto.getTranslation())
                            .build()));
        }

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify update of examples
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
        Long cardId = 1L;

        TranslationDto[] newTranslations = {
            new TranslationDto(null, "newTranslation1"), new TranslationDto(null, "newTranslation2")
        };

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(newTranslations)
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        Learner user = new Learner();

        Card card = Card.builder()
                .id(cardId)
                .translations(new ArrayList<>())
                .cardTags(Set.of())
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify creation of new translations
        for (TranslationDto newTranslation : newTranslations) {
            verify(translationRepository)
                    .save(argThat(translation -> translation.getTranslation().equals(newTranslation.getTranslation())
                            && translation.getCard().equals(card)));
        }
    }

    @DisplayName("Should create new examples")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenNewExamplesCreated() {
        // Arrange
        Long cardId = 1L;

        ExampleDto[] newExamples = {
            new ExampleDto(null, "newExample1", "newTranslation1"),
            new ExampleDto(null, "newExample2", "newTranslation2")
        };

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[] {})
                .examples(newExamples)
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        Learner user = new Learner();

        Card card = Card.builder()
                .id(cardId)
                .examples(new ArrayList<>())
                .cardTags(Set.of())
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify creation of new examples
        for (ExampleDto newExample : newExamples) {
            verify(exampleRepository)
                    .save(argThat(example -> example.getExample().equals(newExample.getExample())
                            && example.getTranslation().equals(newExample.getTranslation())
                            && example.getCard().equals(card)));
        }
    }

    @DisplayName("Should update the timestamp of the card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTimestampUpdated() {
        // Arrange
        Long cardId = 1L;
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        Learner user = new Learner();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify that the updated timestamp was set
        assertThat(card.getUpdated()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
    }

    @DisplayName("Should save the updated card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenCardSaved() {
        // Arrange
        Long cardId = 1L;
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(new TagDto[] {})
                .deletedTranslationsIds(new int[] {})
                .deletedExamplesIds(new int[] {})
                .build();

        Learner user = new Learner();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

        // Assert
        // Verify that the updated card was saved
        verify(cardRepository).save(card);
    }

    @DisplayName("Should update tags for a given card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTagsUpdated() {
        // Arrange
        Long cardId = 7L;
        String tagToBeDeleted = "tagtobedeleted";
        String oldTag1 = "oldtag1";
        String oldTag2 = "oldtag2";
        String newTag = "newtag";

        CardUpdateDto dto = createCardUpdateDto(cardId, oldTag1, oldTag2, newTag);
        Learner learner = new Learner();
        Card card = Card.builder().id(cardId).build();

        CardTag cardTagToBeDeleted = createCardTag(card, learner, tagToBeDeleted);
        CardTag oldCardTag1 = createCardTag(card, learner, oldTag1);
        CardTag oldCardTag2 = createCardTag(card, learner, oldTag2);
        HashSet<CardTag> existingCardTagsSet = Sets.newHashSet(cardTagToBeDeleted, oldCardTag1, oldCardTag2);
        card.setCardTags(existingCardTagsSet);

        mockCardTagRepository(List.of(cardTagToBeDeleted));

        // Act
        cardServiceImpl.updateCard(cardId, dto, learner);

        // Assert
        verify(cardTagRepository).delete(cardTagToBeDeleted);
        verify(cardTagRepository, never()).delete(oldCardTag1);
        verify(cardTagRepository, never()).delete(oldCardTag2);
        assertNewTagAddition(newTag);
    }

    @DisplayName("Should create a new card with all associated entities")
    @Test
    void givenCardCreationDtoAndLearner_whenCreateCard_thenCardCreatedWithAllEntities() {
        // Arrange
        CardCreationDto mockDto = mock(CardCreationDto.class);
        Card mockCard = mock(Card.class);
        List<Example> mockExamples = Collections.singletonList(mock(Example.class));
        List<Translation> mockTranslations = Collections.singletonList(mock(Translation.class));

        TagDto[] mockTags = {
            TagDto.builder().text("text1").build(),
            TagDto.builder().text("text2").build()
        };

        when(cardMapper.cardDtoToEntity(mockDto)).thenReturn(mockCard);
        when(tagRepository.findByTextWithNormalization(eq("text1"))).thenReturn(Optional.empty());
        when(tagRepository.findByTextWithNormalization(eq("text2")))
                .thenReturn(Optional.of(Tag.builder().id(22L).text("text2").build()));
        when(mockCard.getExamples()).thenReturn(mockExamples);
        when(mockCard.getTranslations()).thenReturn(mockTranslations);
        when(mockDto.getTags()).thenReturn(mockTags);

        Learner mockLearner = mock(Learner.class);

        // Act
        cardServiceImpl.createCard(mockLearner, mockDto);

        // Verify
        verify(cardTagRepository).saveAll(captor.capture());
        List<CardTag> capturedCardTags = captor.getValue();
        assertThat(capturedCardTags.size()).isEqualTo(2);
        assertThat(capturedCardTags.get(0).getTag().getText()).isEqualTo("text1");
        assertThat(capturedCardTags.get(1).getTag().getText()).isEqualTo("text2");

        verify(mockLearner).addCard(mockCard);
        verify(tagRepository).save(eq((new Tag("text1"))));
        verify(tagRepository).findByTextWithNormalization(eq("text1"));
        verify(tagRepository).findByTextWithNormalization(eq("text2"));
        verify(translationRepository).saveAll(mockTranslations);
        verify(exampleRepository).saveAll(mockExamples);
        verify(learnerRepository).save(mockLearner);
    }

    @DisplayName("Should return user's cards of the specified language")
    @Test
    void givenUserAndLanguageCode_whenGetUsersCardsOfLang_thenReturnRightCards() {
        // Mocked data
        Learner user = new Learner();

        List<Card> mockedCards = List.of(
                Card.builder().id(1L).build(),
                Card.builder().id(2L).build(),
                Card.builder().id(3L).build());
        when(cardRepository.findAllByOwnerAndLanguage(user, Language.DE)).thenReturn(mockedCards);

        List<CardDto> mockedCardDtos = List.of(
                CardDto.builder().id(1L).build(),
                CardDto.builder().id(2L).build(),
                CardDto.builder().id(3L).build());
        for (int i = 0; i < mockedCards.size(); i++) {
            when(cardMapper.cardEntityToDto(eq(mockedCards.get(i)))).thenReturn(mockedCardDtos.get(i));
        }

        // Invoke the method
        List<CardDto> result = cardServiceImpl.getUsersCardsOfLang(Language.DE.name(), user);

        // Assertions
        assertThat(result).hasSize(mockedCardDtos.size()).containsExactlyElementsOf(mockedCardDtos);
    }

    private CardUpdateDto createCardUpdateDto(Long cardId, String... tags) {
        return CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(Arrays.stream(tags).map(TagDto::new).toArray(TagDto[]::new))
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
        when(cardRepository.findById(anyLong()))
                .thenReturn(Optional.of(existingCardTags.iterator().next().getCard()));
        existingCardTags.forEach(cardTag -> when(cardTagRepository.getByCardAndText(
                        eq(cardTag.getCard()), eq(cardTag.getTag().getText())))
                .thenReturn(cardTag));
    }

    private void assertNewTagAddition(String newTag) {
        ArgumentCaptor<CardTag> argumentCaptor = ArgumentCaptor.forClass(CardTag.class);
        verify(cardTagRepository, times(1)).save(argumentCaptor.capture());

        List<CardTag> capturedTags = argumentCaptor.getAllValues();
        assertThat(capturedTags)
                .extracting(CardTag::getTag)
                .extracting(Tag::getText)
                .containsExactlyInAnyOrder(newTag);
    }
}

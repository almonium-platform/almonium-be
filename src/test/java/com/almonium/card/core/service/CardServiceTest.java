package com.almonium.card.core.service;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.ExampleDto;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.TranslationDto;
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
import com.google.common.collect.Sets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
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

        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).language(language).build();

        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId1 = UUID.randomUUID();
        UUID cardId2 = UUID.randomUUID();
        Card card1 = Card.builder().id(cardId1).entry("test1").build();
        Card card2 = Card.builder().id(cardId2).entry("test2").build();
        List<Card> cards = Arrays.asList(card1, card2);
        String entry = "test";

        when(cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(learner, "%test%"))
                .thenReturn(cards);

        when(cardMapper.cardEntityToDto(card1))
                .thenReturn(CardDto.builder().id(cardId1).build());
        when(cardMapper.cardEntityToDto(card2))
                .thenReturn(CardDto.builder().id(cardId2).build());

        // Act
        List<CardDto> result = cardService.searchByEntry(entry, language, user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(cardId1);
        assertThat(result.get(1).getId()).isEqualTo(cardId2);
    }

    @DisplayName("Should return CardDto when getCardById is called")
    @Test
    void givenCardId_whenGetCardById_thenReturnCardDto() {
        // Arrange
        UUID id = UUID.randomUUID();
        Card card = Card.builder().id(id).build();
        CardDto expectedDto = CardDto.builder().id(id).build();

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
        when(cardMapper.cardEntityToDto(card)).thenReturn(expectedDto);

        // Act
        CardDto result = cardService.getCardById(id);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Should return CardDto when getCardByPublicId is called")
    void givenCardHash_whenGetCardByPublicId_thenReturnCardDto() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Card card = Card.builder().publicId(uuid).build();
        CardDto expectedDto = CardDto.builder().id(uuid).build();

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

        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();

        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId1 = UUID.randomUUID();
        UUID cardId2 = UUID.randomUUID();
        Card card1 = Card.builder().id(cardId1).build();
        Card card2 = Card.builder().id(cardId2).build();
        List<Card> cards = Arrays.asList(card1, card2);

        CardDto dto1 = CardDto.builder().id(cardId1).build();
        CardDto dto2 = CardDto.builder().id(cardId2).build();
        List<CardDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(cardRepository.findAllByOwner(learner)).thenReturn(cards);
        when(cardMapper.cardEntityToDto(card1)).thenReturn(dto1);
        when(cardMapper.cardEntityToDto(card2)).thenReturn(dto2);

        // Act
        List<CardDto> result = cardService.getUsersCardsOfLang(user, language);

        // Assert
        assertThat(result).isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("Should throw exception when getCardByPublicId is called with non-existent UUID")
    void givenNonExistentCardHash_whenGetCardByPublicId_thenThrowException() {
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

    @Test
    @DisplayName("Should delete specified examples")
    void givenCardUpdateDto_whenUpdateCard_thenExamplesDeleted() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        UUID[] deletedExamplesIds = {UUID.randomUUID(), UUID.randomUUID()};

        List<Example> examples = new ArrayList<>();
        examples.add(Example.builder().id(deletedExamplesIds[0]).build());
        examples.add(Example.builder().id(deletedExamplesIds[1]).build());

        Card card = Card.builder().id(cardId).examples(examples).build();
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .deletedExamplesIds(deletedExamplesIds)
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(User.builder().id(UUID.randomUUID()).build(), dto);

        // Assert
        for (UUID id : deletedExamplesIds) {
            verify(exampleRepository).deleteById(id);
        }
    }

    @DisplayName("Should delete specified translations")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenTranslationsDeleted() {
        // Arrange
        Language language = Language.EN;

        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();

        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId = UUID.randomUUID();
        UUID translationId1 = UUID.randomUUID();
        UUID translationId2 = UUID.randomUUID();
        UUID translationId3 = UUID.randomUUID();

        UUID[] deletedTranslationsIds = {translationId2, translationId3};

        List<Translation> translations = new ArrayList<>();
        translations.add(
                Translation.builder().id(translationId1).translation("trans1").build());
        translations.add(
                Translation.builder().id(translationId2).translation("trans2").build());
        translations.add(
                Translation.builder().id(translationId3).translation("trans3").build());

        Card card = Card.builder()
                .id(cardId)
                .translations(translations)
                .cardTags(Set.of())
                .build();

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .deletedTranslationsIds(deletedTranslationsIds)
                .deletedExamplesIds(new UUID[] {})
                .tags(new TagDto[] {})
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(user, dto);

        // Assert
        for (UUID id : deletedTranslationsIds) {
            verify(translationRepository).deleteById(id);
        }
    }

    @Test
    @DisplayName("Should update existing translations")
    void givenCardUpdateDto_whenUpdateCard_thenExistingTranslationsUpdated() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        TranslationDto[] updatedTranslations = {
            new TranslationDto(UUID.randomUUID(), "updatedTranslation1"),
            new TranslationDto(UUID.randomUUID(), "updatedTranslation2")
        };

        List<Translation> originalTranslations = new ArrayList<>();
        for (TranslationDto t : updatedTranslations) {
            originalTranslations.add(Translation.builder().id(t.getId()).build());
        }

        Card card = Card.builder().id(cardId).translations(originalTranslations).build();
        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .translations(updatedTranslations)
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        for (TranslationDto t : updatedTranslations) {
            when(translationRepository.findById(t.getId()))
                    .thenReturn(Optional.of(Translation.builder()
                            .id(t.getId())
                            .translation(t.getTranslation())
                            .build()));
        }

        // Act
        cardService.updateCard(User.builder().id(UUID.randomUUID()).build(), dto);

        // Assert
        for (TranslationDto updatedTranslation : updatedTranslations) {
            verify(translationRepository)
                    .save(argThat(tr -> tr.getId().equals(updatedTranslation.getId())
                            && tr.getTranslation().equals(updatedTranslation.getTranslation())));
        }
    }

    @DisplayName("Should update existing examples")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenExistingExamplesUpdated() {
        // Arrange
        Language language = Language.EN;

        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();

        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId = UUID.randomUUID();
        UUID exampleId1 = UUID.randomUUID();
        UUID exampleId2 = UUID.randomUUID();

        ExampleDto[] updatedExamples = {
            new ExampleDto(exampleId1, "updatedExample1", "updatedTranslation1"),
            new ExampleDto(exampleId2, "updatedExample2", "updatedTranslation2")
        };

        List<Example> originalExamples = new ArrayList<>();
        originalExamples.add(Example.builder()
                .id(exampleId1)
                .example("originalExample1")
                .translation("originalTranslation1")
                .build());
        originalExamples.add(Example.builder()
                .id(exampleId2)
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
                .deletedTranslationsIds(new UUID[] {})
                .deletedExamplesIds(new UUID[] {})
                .build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking DB fetch for each example update
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

    @Test
    @DisplayName("Should create new translations")
    void givenCardUpdateDto_whenUpdateCard_thenNewTranslationsCreated() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        TranslationDto[] newTranslations = {
            new TranslationDto(null, "newTranslation1"), new TranslationDto(null, "newTranslation2")
        };

        Card card = Card.builder().id(cardId).translations(new ArrayList<>()).build();
        CardUpdateDto dto =
                CardUpdateDto.builder().id(cardId).translations(newTranslations).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardService.updateCard(User.builder().id(UUID.randomUUID()).build(), dto);

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
        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId = UUID.randomUUID();
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
        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId = UUID.randomUUID();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();
        CardUpdateDto dto =
                CardUpdateDto.builder().id(cardId).language(language).build();

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
        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId = UUID.randomUUID();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();
        CardUpdateDto dto =
                CardUpdateDto.builder().id(cardId).language(language).build();

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
        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();
        when(learnerFinder.findLearner(user, language)).thenReturn(learner);

        UUID cardId = UUID.randomUUID();
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

    @DisplayName("Should return user's cards of the specified language")
    @Test
    void givenUserAndLanguageCode_whenGetUsersCardsOfLang_thenReturnRightCards() {
        // Arrange
        Language testLanguage = Language.DE;
        UUID userId = UUID.randomUUID();
        UUID learnerId = UUID.randomUUID();
        User user = User.builder().id(userId).build();
        Learner learner = Learner.builder().id(learnerId).build();
        when(learnerFinder.findLearner(user, testLanguage)).thenReturn(learner);

        List<Card> mockedCards = List.of(
                Card.builder().id(UUID.randomUUID()).build(),
                Card.builder().id(UUID.randomUUID()).build(),
                Card.builder().id(UUID.randomUUID()).build());
        when(cardRepository.findAllByOwner(learner)).thenReturn(mockedCards);

        List<CardDto> mockedCardDtos = new ArrayList<>();
        for (Card card : mockedCards) {
            mockedCardDtos.add(CardDto.builder().id(card.getId()).build());
            when(cardMapper.cardEntityToDto(card))
                    .thenReturn(CardDto.builder().id(card.getId()).build());
        }

        // Act
        List<CardDto> result = cardService.getUsersCardsOfLang(user, testLanguage);

        // Assert
        assertThat(result).hasSize(mockedCardDtos.size()).containsExactlyElementsOf(mockedCardDtos);
    }
    // -------------------------------------------------------------------------
    // Helper methods for building DTOs / mocks
    // -------------------------------------------------------------------------

    private CardUpdateDto createCardUpdateDto(UUID cardId, Language language, String... tags) {
        TagDto[] tagDtos = Arrays.stream(tags).map(TagDto::new).toArray(TagDto[]::new);
        return CardUpdateDto.builder()
                .id(cardId)
                .language(language)
                .translations(new TranslationDto[] {})
                .examples(new ExampleDto[] {})
                .tags(tagDtos)
                .deletedTranslationsIds(new UUID[] {})
                .deletedExamplesIds(new UUID[] {})
                .build();
    }

    private CardTag createCardTag(Card card, Learner learner, String tagText) {
        return CardTag.builder()
                .id(new CardTagPK(card.getId(), UUID.randomUUID()))
                .card(card)
                .tag(new Tag(tagText))
                .learner(learner)
                .build();
    }

    private void mockCardTagRepository(List<CardTag> existingCardTags) {
        // Assume all tags belong to the same card
        Card card = existingCardTags.iterator().next().getCard();
        when(cardRepository.findById(any(UUID.class))).thenReturn(Optional.of(card));

        existingCardTags.forEach(cardTag -> when(cardTagRepository.getByCardAndText(
                        card, cardTag.getTag().getText()))
                .thenReturn(cardTag));
    }
}

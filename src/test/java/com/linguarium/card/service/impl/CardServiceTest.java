package com.linguarium.card.service.impl;

import com.linguarium.card.dto.*;
import com.linguarium.card.mapper.CardMapper;
import com.linguarium.card.model.*;
import com.linguarium.card.repository.*;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.model.User;
import com.linguarium.user.repository.LearnerRepository;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cardServiceImpl = new CardServiceImpl(cardRepository, cardTagRepository, tagRepository, exampleRepository, translationRepository, learnerRepository, cardMapper);
    }

    @Test
    @DisplayName("Should return a list of CardDto that match the search entry")
    public void givenSearchEntryAndUser_whenSearchByEntry_thenReturnMatchingCards() {
        // Arrange
        String entry = "test";
        Learner user = new Learner();
        Card card1 = Card.builder().id(1L).build();
        card1.setEntry("test1");
        Card card2 = Card.builder().id(2L).build();
        card2.setEntry("test2");
        List<Card> cards = Arrays.asList(card1, card2);

        when(cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(user, "%test%")).thenReturn(cards);
        when(cardMapper.cardEntityToDto(card1)).thenReturn(new CardDto());
        when(cardMapper.cardEntityToDto(card2)).thenReturn(new CardDto());

        // Act
        List<CardDto> result = cardServiceImpl.searchByEntry(entry, user);

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
        CardDto result = cardServiceImpl.getCardById(id);

        // Assert
        assertThat(result).isEqualTo(expectedDto);
    }

    @Test
    @DisplayName("Should return CardDto when getCardByHash is called")
    public void givenCardHash_whenGetCardByHash_thenReturnCardDto() {
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

    @Test
    @DisplayName("Should return list of CardDto when getUsersCards is called")
    public void givenUser_whenGetUsersCards_thenReturnListOfCardDto() {
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

    @Test
    @DisplayName("Should throw exception when getCardByHash is called with non-existent hash")
    public void givenNonExistentCardHash_whenGetCardByHash_thenThrowException() {
        // Arrange
        UUID random = UUID.randomUUID();

        when(cardRepository.getByPublicId(random)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> cardServiceImpl.getCardByPublicId(random.toString()));
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

        Learner user = new Learner();

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
        cardServiceImpl.updateCard(dto, user);

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

        Learner user = new Learner();

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
        cardServiceImpl.updateCard(dto, user);

        // Assert
        // Verify deletion of translations
        for (int id : tr_del) {
            verify(translationRepository).deleteById((long) id);
        }
    }

    @Test
    @DisplayName("Should delete card by ID")
    public void givenCardId_whenDeleteById_thenCardIsDeleted() {
        Long id = 1L;

        // Act
        cardServiceImpl.deleteById(id);

        // Assert
        verify(cardRepository, times(1)).deleteById(id);
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

        Learner user = new Learner();

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
        cardServiceImpl.updateCard(dto, user);

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

        Learner user = new Learner();

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
        cardServiceImpl.updateCard(dto, user);

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

        Learner user = new Learner();

        Card card = Card.builder().id(cardId)
                .translations(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardServiceImpl.updateCard(dto, user);

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

        Learner user = new Learner();

        Card card = Card.builder().id(cardId)
                .examples(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardServiceImpl.updateCard(dto, user);

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

        Learner user = new Learner();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardServiceImpl.updateCard(dto, user);

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

        Learner user = new Learner();
        Card card = Card.builder().id(cardId).cardTags(Set.of()).build();

        when(cardRepository.getById(cardId)).thenReturn(card);

        // Act
        cardServiceImpl.updateCard(dto, user);

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

        Learner learner = new Learner();
        Card card = Card.builder().id(cardId).build();

        // Mock existing tags on the card
        CardTag tag3 = CardTag.builder().tag(new Tag("tag3")).card(card).learner(learner).build();
        CardTag tag4 = CardTag.builder().tag(new Tag("tag4")).card(card).learner(learner).build();
        CardTag tag1 = CardTag.builder().tag(new Tag("tag1")).card(card).learner(learner).build(); // This tag should persist

        Set<CardTag> existingTags = new HashSet<>(Arrays.asList(tag3, tag4, tag1));
        card.setCardTags(existingTags);

        when(cardRepository.getById(cardId)).thenReturn(card);
        when(cardTagRepository.getByCardAndText(eq(card), eq("tag3"))).thenReturn(tag3);
        when(cardTagRepository.getByCardAndText(eq(card), eq("tag4"))).thenReturn(tag4);

        // Act
        cardServiceImpl.updateCard(dto, learner);

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
    @DisplayName("Should return user's cards of the specified language")
    void givenUserAndLanguageCode_whenGetUsersCardsOfLang_thenReturnRightCards() {
        // Mocked data
        Learner user = new Learner();

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
        when(cardRepository.findAllByOwnerAndLanguage(user, Language.DE)).thenReturn(mockedCards);

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
        List<CardDto> result = cardServiceImpl.getUsersCardsOfLang(Language.DE.name(), user);

        // Assertions
        assertThat(result).hasSize(mockedCardDtos.size()).containsExactlyElementsOf(mockedCardDtos);
    }
}
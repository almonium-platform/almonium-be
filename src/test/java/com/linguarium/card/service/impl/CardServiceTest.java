package com.linguarium.card.service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(cardRepository.findById(id)).thenReturn(Optional.of(card));
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
    @DisplayName("Should delete card by ID")
    public void givenCardId_whenDeleteById_thenCardIsDeleted() {
        Long id = 1L;

        // Act
        cardServiceImpl.deleteById(id);

        // Assert
        verify(cardRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Should delete specified examples")
    public void givenCardUpdateDto_whenUpdateCard_thenExamplesDeleted() {
        // Arrange
        Long cardId = 1L;
        int[] ex_del = {4, 5};  // IDs of examples to be deleted

        CardUpdateDto dto = CardUpdateDto.builder()
                .id(cardId)
                .deletedExamplesIds(ex_del)
                .deletedTranslationsIds(new int[]{})
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

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

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
                .deletedTranslationsIds(tr_del)
                .deletedExamplesIds(new int[]{})
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

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

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
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
                .build();

        Learner user = new Learner();

        // Create a list of translations, some of which will be updated
        List<Translation> originalTranslations = new ArrayList<>();
        originalTranslations.add(Translation.builder().id(1L).translation("originalTranslation1").build());
        originalTranslations.add(Translation.builder().id(2L).translation("originalTranslation2").build());

        Card card = Card.builder().id(cardId)
                .translations(originalTranslations)
                .cardTags(Set.of()).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking translation retrieval
        for (TranslationDto translationDto : updatedTranslations) {
            when(translationRepository.findById(translationDto.getId())).thenReturn(
                    Optional.of(Translation.builder()
                            .id(translationDto.getId())
                            .translation(translationDto.getTranslation())
                            .build()));
        }

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

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
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
                .build();

        Learner user = new Learner();

        // Create a list of examples, some of which will be updated
        List<Example> originalExamples = new ArrayList<>();
        originalExamples.add(Example.builder().id(1L).example("originalExample1").translation("originalTranslation1").build());
        originalExamples.add(Example.builder().id(2L).example("originalExample2").translation("originalTranslation2").build());

        Card card = Card.builder().id(cardId)
                .examples(originalExamples)
                .cardTags(Set.of()).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Mocking example retrieval
        for (ExampleDto exampleDto : updatedExamples) {
            when(exampleRepository.findById(exampleDto.getId())).thenReturn(Optional.of(
                    Example.builder()
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
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
                .build();

        Learner user = new Learner();

        Card card = Card.builder().id(cardId)
                .translations(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

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
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
                .build();

        Learner user = new Learner();

        Card card = Card.builder().id(cardId)
                .examples(new ArrayList<>())
                .cardTags(Set.of()).build();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Act
        cardServiceImpl.updateCard(cardId, dto, user);

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
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
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
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
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

    @Test
    @DisplayName("Should update tags for a given card")
    public void givenCardUpdateDto_whenUpdateCard_thenTagsUpdated() {
        // Arrange
        Long cardId = 7L;
        String tagToBeDeleted = "tagtobedeleted";
        String oldTag1 = "oldtag1";
        String oldTag2 = "oldtag2";
        String newTag = "newtag";

        CardUpdateDto dto = createCardUpdateDto(cardId, oldTag1, oldTag2, newTag);
        Learner learner = new Learner();
        Card card = Card.builder().id(cardId).build();

        Set<CardTag> existingCardTags = createExistingCardTags(card, learner, oldTag1, oldTag2, tagToBeDeleted);
        card.setCardTags(existingCardTags);

        mockCardTagRepository(existingCardTags);

        // Deep copy of existingCardTags
        Set<CardTag> existingCardTagsCopy = deepCopyCardTags(existingCardTags);

        // Act
        cardServiceImpl.updateCard(cardId, dto, learner);

        // Assert
        assertTagDeletion(existingCardTagsCopy, tagToBeDeleted);
        assertTagPersistence(existingCardTagsCopy, tagToBeDeleted);
        assertNewTagAddition(newTag);
    }


    @Test
    @DisplayName("Should create a new card with all associated entities")
    public void givenCardCreationDtoAndLearner_whenCreateCard_thenCardCreatedWithAllEntities() {
        // Arrange
        Learner mockLearner = mock(Learner.class);
        CardCreationDto mockDto = mock(CardCreationDto.class);
        Card mockCard = mock(Card.class);
        List<Example> mockExamples = Collections.singletonList(mock(Example.class));
        List<Translation> mockTranslations = Collections.singletonList(mock(Translation.class));
        TagDto[] mockTags = {
                TagDto.builder()
                        .text("text1")
                        .build(),
                TagDto.builder()
                        .text("text2")
                        .build()
        };

        when(cardMapper.cardDtoToEntity(mockDto)).thenReturn(mockCard);
        when(tagRepository.findByTextWithNormalization(eq("text1"))).thenReturn(Optional.empty());
        when(tagRepository.findByTextWithNormalization(eq("text2"))).thenReturn(Optional.of(
                Tag.builder()
                        .id(22L)
                        .text("text2")
                        .build()));
        when(mockCard.getExamples()).thenReturn(mockExamples);
        when(mockCard.getTranslations()).thenReturn(mockTranslations);
        when(mockDto.getTags()).thenReturn(mockTags);

        // Act
        cardServiceImpl.createCard(mockLearner, mockDto);


        // Verify
        ArgumentCaptor<List<CardTag>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(cardTagRepository).saveAll(argumentCaptor.capture());
        List<CardTag> capturedCardTags = argumentCaptor.getValue();
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

    private CardUpdateDto createCardUpdateDto(Long cardId, String... tags) {
        return CardUpdateDto.builder()
                .id(cardId)
                .translations(new TranslationDto[]{})
                .examples(new ExampleDto[]{})
                .tags(Arrays.stream(tags).map(TagDto::new).toArray(TagDto[]::new))
                .deletedTranslationsIds(new int[]{})
                .deletedExamplesIds(new int[]{})
                .build();
    }

    private Set<CardTag> createExistingCardTags(Card card, Learner learner, String... tags) {
        return Arrays.stream(tags)
                .map(tagText -> CardTag.builder()
                        .id(new CardTagPK(card.getId(), new Random().nextLong()))  // Random ID for demonstration
                        .card(card)
                        .tag(new Tag(tagText))
                        .learner(learner)
                        .build())
                .collect(Collectors.toSet());
    }

    private void mockCardTagRepository(Set<CardTag> existingCardTags) {
        when(cardRepository.findById(anyLong())).thenReturn(Optional.of(existingCardTags.iterator().next().getCard()));
        existingCardTags.forEach(
                cardTag -> when(cardTagRepository.getByCardAndText(
                        eq(cardTag.getCard()),
                        eq(cardTag.getTag().getText())
                )).thenReturn(cardTag)
        );
    }

    private Set<CardTag> deepCopyCardTags(Set<CardTag> existingCardTags) {
        return existingCardTags.stream()
                .map(cardTag -> CardTag.builder()
                        .id(cardTag.getId())
                        .card(cardTag.getCard())
                        .tag(cardTag.getTag())
                        .learner(cardTag.getLearner())
                        .build())
                .collect(Collectors.toSet());
    }

    private void assertTagDeletion(Set<CardTag> existingCardTags, String tagToBeDeleted) {
        verify(cardTagRepository).delete(existingCardTags.stream().filter(x
                -> x.getTag().getText().equals(tagToBeDeleted)).findAny().orElseThrow());
    }

    private void assertTagPersistence(Set<CardTag> existingCardTags, String tagToBeDeleted) {
        verify(cardTagRepository, never()).delete(existingCardTags.stream().filter(x
                -> !x.getTag().getText().equals(tagToBeDeleted)).findAny().orElseThrow());
    }

    private void assertNewTagAddition(String newTag) {
        ArgumentCaptor<CardTag> argumentCaptor = ArgumentCaptor.forClass(CardTag.class);
        verify(cardTagRepository, times(1)).save(argumentCaptor.capture());

        List<CardTag> capturedTags = argumentCaptor.getAllValues();
        assertThat(capturedTags).extracting(CardTag::getTag)
                .extracting(Tag::getText)
                .containsExactlyInAnyOrder(newTag);
    }
}
package com.almonium.card.core.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.TagDto;
import com.almonium.card.core.dto.request.CardCreationDto;
import com.almonium.card.core.dto.request.CardUpdateDto;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.mapper.CardMapper;
import com.almonium.card.core.model.entity.CardTag;
import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Tag;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.model.entity.pk.CardTagPK;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.card.core.model.enums.LearningItemType;
import com.almonium.card.core.repository.CardTagRepository;
import com.almonium.card.core.repository.ExampleRepository;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.repository.TagRepository;
import com.almonium.card.core.repository.TranslationRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import com.google.common.collect.Sets;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CardService {
    LearnerFinder learnerFinder;

    LearningItemRepository learningItemRepository;
    CardTagRepository cardTagRepository;
    TagRepository tagRepository;
    ExampleRepository exampleRepository;
    TranslationRepository translationRepository;
    LearnerRepository learnerRepository;

    CardMapper cardMapper;

    public CardDto getCardById(User user, UUID id) {
        return cardMapper.cardEntityToDto(findOwnedCard(user, id));
    }

    public CardDto getCardByPublicId(String hash) {
        LearningItem card =
                learningItemRepository.getByPublicId(UUID.fromString(hash)).orElseThrow();
        CardDto dto = cardMapper.cardEntityToDto(card);
        dto.setSourceContext(null);
        dto.setLearningIntents(Set.of());
        return dto;
    }

    public List<CardDto> getUsersCardsOfLang(User user, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        return learningItemRepository.findAllByOwner(learner).stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CardDto> searchByEntry(String entry, Language language, User user) {
        Learner learner = learnerFinder.findLearner(user, language);

        return learningItemRepository
                .findAllByOwnerAndEntryLikeIgnoreCase(
                        learner, '%' + entry.trim().toLowerCase() + '%')
                .stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createCard(User user, CardCreationDto dto) {
        Language language = dto.getLanguage();
        Learner learner = learnerFinder.findLearner(user, language);
        LearningItem card = initializeCard(learner, dto);
        List<CardTag> cardTags = createCardTags(card, dto.getTags());
        saveEntities(card, card.getTranslations(), card.getExamples(), cardTags, learner);
        log.info("Created card {} for user {}", card, learner);
    }

    @Transactional
    public void updateCard(User user, CardUpdateDto dto) {
        Language language = dto.getLanguage();
        Learner learner = learnerFinder.findLearner(user, language);
        LearningItem entity = findOwnedCard(user, dto.getId());
        updateCardDetails(entity, dto);
        updateTags(entity, dto.getTags(), learner);
        entity.setUpdatedAt(Instant.now());
        learningItemRepository.save(entity);
    }

    @Transactional
    public void deleteById(User user, UUID id) {
        learningItemRepository.delete(findOwnedCard(user, id));
    }

    public void deleteByLanguage(Language code, Learner learner) {
        learningItemRepository.deleteAllByOwnerAndLanguage(learner, code);
    }

    private LearningItem initializeCard(Learner learner, CardCreationDto dto) {
        LearningItem card = cardMapper.cardDtoToEntity(dto);
        if (card.getExamples() == null) {
            card.setExamples(new ArrayList<>());
        }
        card.setCreatedAt(Instant.now());
        card.setUpdatedAt(Instant.now());
        card.setNormalizedForm(normalize(card.getEntry()));
        if (card.getItemType() == null) {
            card.setItemType(LearningItemType.WORD);
        }
        if (card.getLearningIntents() == null || card.getLearningIntents().isEmpty()) {
            card.setLearningIntents(new HashSet<>(Set.of(LearningIntent.UNDERSTAND)));
        }
        card.setDueAt(Instant.now());
        learner.addLearningItem(card);
        if (learner.getUser() != null) {
            card.setLegacyOwnerId(learner.getUser().getId());
        }
        card.getTranslations().forEach(translation -> translation.setCard(card));
        card.getExamples().forEach(example -> example.setCard(card));
        return card;
    }

    private List<CardTag> createCardTags(LearningItem card, TagDto[] tagDtos) {
        List<CardTag> cardTags = new ArrayList<>();
        if (tagDtos == null) {
            return cardTags;
        }
        for (TagDto tagDto : tagDtos) {
            CardTag cardTag = new CardTag();
            cardTag.setCard(card);
            cardTag.setLearner(card.getOwner());
            cardTag.setTag(findOrCreateTag(tagDto.getText()));
            cardTags.add(cardTag);
        }
        return cardTags;
    }

    private Tag findOrCreateTag(String text) {
        return tagRepository.findByTextWithNormalization(text).orElseGet(() -> {
            Tag tag = new Tag(text);
            tagRepository.save(tag);
            return tag;
        });
    }

    private void saveEntities(
            LearningItem card,
            List<Translation> translations,
            List<Example> examples,
            List<CardTag> cardTags,
            Learner learner) {
        learningItemRepository.save(card);
        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        cardTagRepository.saveAll(cardTags);
        learnerRepository.save(learner);
    }

    private void updateCardDetails(LearningItem entity, CardUpdateDto dto) {
        cardMapper.update(dto, entity);
        if (dto.getEntry() != null) {
            entity.setNormalizedForm(normalize(dto.getEntry()));
        }

        Optional.ofNullable(dto.getDeletedTranslationsIds()).ifPresent(ids -> Arrays.stream(ids)
                .map(id -> findTranslation(entity, id))
                .forEach(translationRepository::delete));

        Optional.ofNullable(dto.getDeletedExamplesIds())
                .ifPresent(ids ->
                        Arrays.stream(ids).map(id -> findExample(entity, id)).forEach(exampleRepository::delete));

        Optional.ofNullable(dto.getTranslations())
                .ifPresent(ids -> Arrays.stream(ids).forEach(translationDto -> {
                    UUID id = translationDto.getId();
                    if (id != null) {
                        Translation translation = findTranslation(entity, id);
                        translation.setTranslation(translationDto.getTranslation());
                        translationRepository.save(translation);
                    } else {
                        translationRepository.save(Translation.builder()
                                .card(entity)
                                .translation(translationDto.getTranslation())
                                .build());
                    }
                }));

        Optional.ofNullable(dto.getExamples())
                .ifPresent(ids -> Arrays.stream(ids).forEach(exampleDto -> {
                    UUID id = exampleDto.getId();
                    if (id != null) {
                        Example example = findExample(entity, id);
                        example.setExample(exampleDto.getExample());
                        example.setTranslation(exampleDto.getTranslation());
                        exampleRepository.save(example);
                    } else {
                        exampleRepository.save(Example.builder()
                                .card(entity)
                                .example(exampleDto.getExample())
                                .translation(exampleDto.getTranslation())
                                .build());
                    }
                }));
    }

    private LearningItem findOwnedCard(User user, UUID cardId) {
        return learningItemRepository
                .findByIdAndOwnerUserId(cardId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Card not found: " + cardId));
    }

    private Translation findTranslation(LearningItem card, UUID translationId) {
        return card.getTranslations().stream()
                .filter(translation -> translationId.equals(translation.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Translation not found on card: " + translationId));
    }

    private Example findExample(LearningItem card, UUID exampleId) {
        return card.getExamples().stream()
                .filter(example -> exampleId.equals(example.getId()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Example not found on card: " + exampleId));
    }

    private void updateTags(LearningItem entity, TagDto[] tagDtos, Learner learner) {
        HashSet<String> dtoTagSet = Optional.ofNullable(tagDtos)
                .map(tags -> Arrays.stream(tags).map(TagDto::getText).collect(Collectors.toCollection(HashSet::new)))
                .orElseGet(HashSet::new);

        HashSet<String> cardTagSet = entity.getCardTags().stream()
                .map(cardTag -> cardTag.getTag().getText())
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> added = Sets.difference(dtoTagSet, cardTagSet);
        Set<String> deleted = Sets.difference(cardTagSet, dtoTagSet);

        for (String tagText : deleted) {
            CardTag cardTag = cardTagRepository.getByCardAndText(entity, tagText);
            cardTagRepository.delete(cardTag);
            entity.removeCardTag(cardTag);
        }

        for (String tagText : added) {
            Optional<Tag> tagOptional = tagRepository.findByText(tagText);

            Tag tag = tagOptional.orElseGet(() -> {
                Tag createdTag = new Tag(tagText);
                tagRepository.save(createdTag);
                return createdTag;
            });

            CardTag cardTag = CardTag.builder()
                    .learner(learner)
                    .tag(tag)
                    .card(entity)
                    .id(new CardTagPK(entity.getId(), tag.getId()))
                    .build();
            cardTagRepository.save(cardTag);
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(java.util.Locale.ROOT);
    }
}

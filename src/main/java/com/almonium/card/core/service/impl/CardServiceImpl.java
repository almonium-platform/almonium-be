package com.almonium.card.core.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.dto.CardCreationDto;
import com.almonium.card.core.dto.CardDto;
import com.almonium.card.core.dto.CardUpdateDto;
import com.almonium.card.core.dto.TagDto;
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
import com.almonium.card.core.service.CardService;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.repository.LearnerRepository;
import com.google.common.collect.Sets;
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
public class CardServiceImpl implements CardService {
    CardRepository cardRepository;
    CardTagRepository cardTagRepository;
    TagRepository tagRepository;
    ExampleRepository exampleRepository;
    TranslationRepository translationRepository;
    LearnerRepository learnerRepository;
    CardMapper cardMapper;

    @Override
    public CardDto getCardById(Long id) {
        return cardMapper.cardEntityToDto(cardRepository.findById(id).orElseThrow());
    }

    @Override
    public CardDto getCardByPublicId(String hash) {
        Card card = cardRepository.getByPublicId(UUID.fromString(hash)).orElseThrow();
        return cardMapper.cardEntityToDto(card);
    }

    @Override
    public List<CardDto> getUsersCards(Learner learner) {
        return cardRepository.findAllByOwner(learner).stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getUsersCardsOfLang(Language language, Learner learner) {
        return cardRepository.findAllByOwnerAndLanguage(learner, language).stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<CardDto> searchByEntry(String entry, Learner learner) {
        return cardRepository
                .findAllByOwnerAndEntryLikeIgnoreCase(
                        learner, '%' + entry.trim().toLowerCase() + '%')
                .stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createCard(Learner learner, CardCreationDto dto) {
        Card card = initializeCard(learner, dto);
        List<CardTag> cardTags = createCardTags(card, dto.getTags());
        saveEntities(card, card.getTranslations(), card.getExamples(), cardTags, learner);
        log.info("Created card {} for user {}", card, learner);
    }

    @Override
    @Transactional
    public void updateCard(Long id, CardUpdateDto dto, Learner learner) {
        Card entity = cardRepository.findById(dto.getId()).orElseThrow();
        updateCardDetails(entity, dto);
        updateTags(entity, dto.getTags(), learner);
        entity.setUpdatedAt(Instant.now());
        cardRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    public void deleteByLanguage(Language code, Learner user) {
        cardRepository.deleteAllByOwnerAndLanguage(user, code);
    }

    private Card initializeCard(Learner learner, CardCreationDto dto) {
        Card card = cardMapper.cardDtoToEntity(dto);
        card.setCreatedAt(Instant.now());
        card.setUpdatedAt(Instant.now());
        learner.addCard(card);
        return card;
    }

    private List<CardTag> createCardTags(Card card, TagDto[] tagDtos) {
        List<CardTag> cardTags = new ArrayList<>();
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
            Card card,
            List<Translation> translations,
            List<Example> examples,
            List<CardTag> cardTags,
            Learner learner) {
        cardRepository.save(card);
        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        cardTagRepository.saveAll(cardTags);
        learnerRepository.save(learner);
    }

    private void updateCardDetails(Card entity, CardUpdateDto dto) {
        cardMapper.update(dto, entity);

        Arrays.stream(dto.getDeletedTranslationsIds()).forEach(id -> translationRepository.deleteById((long) id));

        Arrays.stream(dto.getDeletedExamplesIds()).forEach(id -> exampleRepository.deleteById((long) id));

        Arrays.stream(dto.getTranslations()).forEach(translationDto -> {
            Long id = translationDto.getId();
            if (id != null) {
                Translation translation = translationRepository.findById(id).orElseThrow();
                translation.setTranslation(translationDto.getTranslation());
                translationRepository.save(translation);
            } else {
                translationRepository.save(Translation.builder()
                        .card(entity)
                        .translation(translationDto.getTranslation())
                        .build());
            }
        });

        Arrays.stream(dto.getExamples()).forEach(exampleDto -> {
            Long id = exampleDto.getId();
            if (id != null) {
                Example example = exampleRepository.findById(id).orElseThrow();
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
        });
    }

    private void updateTags(Card entity, TagDto[] tagDtos, Learner learner) {
        HashSet<String> dtoTagSet =
                Arrays.stream(tagDtos).map(TagDto::getText).collect(Collectors.toCollection(HashSet::new));

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
}

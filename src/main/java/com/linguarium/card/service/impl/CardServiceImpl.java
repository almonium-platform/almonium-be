package com.linguarium.card.service.impl;

import com.google.common.collect.Sets;
import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.card.dto.TagDto;
import com.linguarium.card.mapper.CardMapper;
import com.linguarium.card.model.*;
import com.linguarium.card.repository.*;
import com.linguarium.card.service.CardService;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.user.repository.LearnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
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
    @Transactional
    public CardDto getCardById(Long id) {
        return cardMapper.cardEntityToDto(cardRepository.getById(id));
    }

    @Override
    @Transactional
    public CardDto getCardByPublicId(String hash) {
        Card card = cardRepository.getByPublicId(UUID.fromString(hash)).orElseThrow();
        return cardMapper.cardEntityToDto(card);
    }

    @Override
    @Transactional
    public List<CardDto> getUsersCards(Learner learner) {
        return cardRepository.findAllByOwner(learner).stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createCard(Learner learner, CardCreationDto dto) {
        Card card = cardMapper.cardDtoToEntity(dto);
        card.setCreated(LocalDateTime.now());
        card.setUpdated(LocalDateTime.now());
        learner.addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(example -> example.setCard(card));

        List<Translation> translations = card.getTranslations();
        translations.forEach(translation -> translation.setCard(card));
        List<CardTag> cardTags = new ArrayList<>();
        TagDto[] tags = dto.getTags();
        Arrays.stream(tags).forEach(tagDto -> {
            CardTag cardTag = new CardTag();
            cardTag.setCard(card);
            cardTag.setLearner(card.getOwner());

            Optional<Tag> tagOptional = tagRepository.findByTextNormalized(tagDto.getText());
            if (tagOptional.isPresent()) {
                cardTag.setTag(tagOptional.get());
            } else {
                Tag tag = new Tag(tagDto.getText());
                tagRepository.save(tag);
                cardTag.setTag(tag);
            }

            cardTags.add(cardTag);
        });

        cardRepository.save(card);

        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        cardTagRepository.saveAll(cardTags);

        learnerRepository.save(learner);
        log.info("Created card {} for user {}", card, learner);
    }

    @Override
    @Transactional
    public List<CardDto> searchByEntry(String entry, Learner learner) {
        return cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(learner, '%' + entry.trim().toLowerCase() + '%')
                .stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateCard(CardUpdateDto dto, Learner learner) {
        Card entity = cardRepository.getById(dto.getId());
        cardMapper.update(dto, entity);

        Arrays.stream(dto.getTr_del()).forEach(i -> translationRepository.deleteById((long) i));
        Arrays.stream(dto.getEx_del()).forEach(i -> exampleRepository.deleteById((long) i));

        Arrays.stream(dto.getTranslations()).forEach(translationDto -> {
            Long id = translationDto.getId();
            if (id != null) {
                Translation translation = translationRepository.getById(id);
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
                Example example = exampleRepository.getById(id);
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

        HashSet<String> dtoTagSet = Arrays
                .stream(dto.getTags())
                .map(TagDto::getText)
                .collect(Collectors.toCollection(HashSet::new));

        HashSet<String> cardTagSet = entity.getCardTags()
                .stream()
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
        entity.setUpdated(LocalDateTime.now());
        cardRepository.save(entity);
    }

    @Override
    @Transactional
    public List<CardDto> getUsersCardsOfLang(String code, Learner learner) {
        return cardRepository
                .findAllByOwnerAndLanguage(learner, Language.valueOf(code))
                .stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        cardRepository.deleteById(id);
    }
}

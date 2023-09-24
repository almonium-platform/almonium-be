package com.linguatool.service;

import com.google.common.collect.Sets;
import com.linguatool.model.dto.CardAcceptanceDto;
import com.linguatool.model.dto.CardSuggestionDto;
import com.linguatool.model.dto.external_api.request.CardCreationDto;
import com.linguatool.model.dto.external_api.request.CardDto;
import com.linguatool.model.dto.external_api.request.CardUpdateDto;
import com.linguatool.model.dto.external_api.request.TagDto;
import com.linguatool.model.entity.lang.*;
import com.linguatool.model.entity.user.User;
import com.linguatool.model.mapping.CardMapper;
import com.linguatool.repository.*;
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
public class CardService {
    CardRepository cardRepository;
    CardSuggestionRepository cardSuggestionRepository;
    CardTagRepository cardTagRepository;
    TagRepository tagRepository;
    ExampleRepository exampleRepository;
    TranslationRepository translationRepository;
    UserRepository userRepository;
    LanguageRepository languageRepository;
    CardMapper cardMapper;

    @Transactional
    public CardDto getCardById(Long id) {
        return cardMapper.cardEntityToDto(cardRepository.getById(id));
    }

    @Transactional
    public CardDto getCardByHash(String hash) {
        Card card = cardRepository.getByHash(hash).orElseThrow();
        return cardMapper.cardEntityToDto(card);
    }

    @Transactional(value = "transactionManager")
    public List<CardDto> getUsersCards(User user) {
        return cardRepository.findAllByOwner(user).stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public void createCard(User user, CardCreationDto dto) {
        Card card = cardMapper.cardDtoToEntity(dto, languageRepository);
        card.setCreated(LocalDateTime.now());
        card.setUpdated(LocalDateTime.now());
        user.addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(example -> example.setCard(card));

        List<Translation> translations = card.getTranslations();
        translations.forEach(translation -> translation.setCard(card));
        List<CardTag> cardTags = new ArrayList<>();
        TagDto[] tags = dto.getTags();
        Arrays.stream(tags).forEach(t -> {
            CardTag cardTag = new CardTag();
            cardTag.setCard(card);
            cardTag.setUser(card.getOwner());

            Optional<Tag> tagOptional = tagRepository.findByTextNormalized(t.getText());
            if (tagOptional.isPresent()) {
                cardTag.setTag(tagOptional.get());
            } else {
                Tag tag = new Tag(t.getText());
                tagRepository.save(tag);
                cardTag.setTag(tag);
            }

            cardTags.add(cardTag);
        });

        cardRepository.save(card);

        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        cardTagRepository.saveAll(cardTags);

        userRepository.save(user);
        log.info("Created card {} for user {}", card, user);
    }

    @Transactional
    public void cloneCard(Card entity, User user) {
        Card card = cardMapper.copyCardDtoToEntity(cardMapper.cardEntityToDto(entity), languageRepository);

        user.addCard(card);

        List<Example> examples = card.getExamples();
        examples.forEach(e -> e.setCard(card));

        List<Translation> translations = card.getTranslations();

        cardRepository.save(card);
        translationRepository.saveAll(translations);
        exampleRepository.saveAll(examples);
        userRepository.save(user);
        log.info("Cloned card {} for user {}", card, user);
    }

    @Transactional
    public List<CardDto> getSuggestedCards(User user) {
        return cardSuggestionRepository.getByRecipient(user)
                .stream()
                .map(sug -> {
                    CardDto dto = cardMapper.cardEntityToDto(sug.getCard());
                    dto.setUserId(sug.getSender().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void declineSuggestion(CardAcceptanceDto dto, User recipient) {
        //TODO Test, changed repo name - entity to just ID
        cardSuggestionRepository
                .deleteBySenderIdAndRecipientIdAndCardId(
                        dto.getSenderId(),
                        recipient.getId(),
                        dto.getCardId()
                );
    }

    @Transactional
    public void acceptSuggestion(CardAcceptanceDto dto, User recipient) {
        User sender = userRepository.getById(dto.getSenderId());
        Card card = cardRepository.getById(dto.getCardId());
        cloneCard(card, recipient);
        CardSuggestion cardSuggestion = cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card);
        cardSuggestionRepository.delete(cardSuggestion);
    }


    public boolean suggestCard(CardSuggestionDto dto, User sender) {
        Card card = cardRepository.getById(dto.getCardId());
        User recipient = userRepository.getById(dto.getRecipientId());
        //TODO  notifications
        //TODO check if has access
        if (cardSuggestionRepository.getBySenderAndRecipientAndCard(sender, recipient, card) == null) {
            cardSuggestionRepository.save(new CardSuggestion(sender, recipient, card));
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public List<CardDto> searchByEntry(String entry, User user) {
        return cardRepository.findAllByOwnerAndEntryLikeIgnoreCase(user, '%' + entry.trim().toLowerCase() + '%')
                .stream().map(cardMapper::cardEntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public void updateCard(CardUpdateDto dto, User user) {
        Card entity = cardRepository.getById(dto.getId());
        cardMapper.update(dto, entity, languageRepository);

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
                    .user(user)
                    .tag(tag)
                    .card(entity)
                    .id(new CardTagPK(entity.getId(), tag.getId()))
                    .build();
            cardTagRepository.save(cardTag);
//            entity.addCardTag(cardTag);
//            tag.addCardTag(cardTag);
        }
        entity.setUpdated(LocalDateTime.now());
        cardRepository.save(entity);
    }

    @Transactional
    public List<CardDto> getUsersCardsOfLang(String code, User user) {
        LanguageEntity language = languageRepository.findByCode(Language.fromString(code))
                .orElseThrow(() -> new NoSuchElementException("Can't find language for code: " + code));
        return cardRepository
                .findAllByOwnerAndLanguage(user, language)
                .stream()
                .map(cardMapper::cardEntityToDto)
                .collect(Collectors.toList());
    }
}

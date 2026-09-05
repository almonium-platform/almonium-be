package com.almonium.card.deck.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.card.deck.dto.request.DeckCreationRequest;
import com.almonium.card.deck.dto.request.DeckUpdateRequest;
import com.almonium.card.deck.dto.response.DeckDto;
import com.almonium.card.deck.model.entity.Deck;
import com.almonium.card.deck.model.entity.DeckItem;
import com.almonium.card.deck.repository.DeckItemRepository;
import com.almonium.card.deck.repository.DeckRepository;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The owner's side of a deck: make it, name it, order its words, and switch its link on or off. */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DeckService {
    DeckRepository deckRepository;
    DeckItemRepository deckItemRepository;
    LearningItemRepository learningItemRepository;
    LearnerFinder learnerFinder;
    ShareIdGenerator shareIdGenerator;

    public List<DeckDto> myDecks(User user) {
        return deckRepository.findAllByOwnerUserIdAndDeletedAtIsNullOrderByUpdatedAtDesc(user.getId()).stream()
                .map(this::toDto)
                .toList();
    }

    public DeckDto getDeck(User user, UUID id) {
        return toDto(findOwnedDeck(user, id));
    }

    @Transactional
    public DeckDto createDeck(User user, DeckCreationRequest request) {
        Learner learner = learnerFinder.findLearner(user, request.language());
        Deck deck = Deck.builder()
                .owner(learner)
                .language(request.language())
                .title(request.title().trim())
                .shareId(freshShareId())
                .build();
        deckRepository.save(deck);
        if (request.wordIds() != null && !request.wordIds().isEmpty()) {
            replaceWords(user, deck, request.wordIds());
        }
        log.info("Created deck {} for user {}", deck.getId(), user.getId());
        return toDto(deck);
    }

    @Transactional
    public DeckDto updateDeck(User user, UUID id, DeckUpdateRequest request) {
        Deck deck = findOwnedDeck(user, id);
        if (request.title() != null && !request.title().isBlank()) {
            deck.setTitle(request.title().trim());
        }
        if (request.shareEnabled() != null) {
            deck.setShareEnabled(request.shareEnabled());
        }
        deck.setUpdatedAt(Instant.now());
        deckRepository.save(deck);
        return toDto(deck);
    }

    @Transactional
    public DeckDto setWords(User user, UUID id, List<UUID> wordIds) {
        Deck deck = findOwnedDeck(user, id);
        replaceWords(user, deck, wordIds);
        deck.setUpdatedAt(Instant.now());
        deckRepository.save(deck);
        return toDto(deck);
    }

    /** Soft: the link keeps answering, and says the deck was deleted rather than that nothing was ever here. */
    @Transactional
    public void deleteDeck(User user, UUID id) {
        Deck deck = findOwnedDeck(user, id);
        deck.setDeletedAt(Instant.now());
        deck.setShareEnabled(false);
        deckRepository.save(deck);
    }

    private void replaceWords(User user, Deck deck, List<UUID> wordIds) {
        List<DeckItem> items = new ArrayList<>();
        int position = 0;
        for (UUID wordId : wordIds.stream().distinct().toList()) {
            LearningItem word = learningItemRepository
                    .findByIdAndOwnerUserId(wordId, user.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Card not found: " + wordId));
            if (word.getLanguage() != deck.getLanguage()) {
                throw new BadUserRequestActionException("A deck holds words of one language.");
            }
            items.add(DeckItem.builder()
                    .deck(deck)
                    .item(word)
                    .position(position++)
                    .build());
        }
        deckItemRepository.deleteAllByDeck(deck);
        deckItemRepository.saveAll(items);
        deck.getItems().clear();
        deck.getItems().addAll(items);
    }

    private String freshShareId() {
        for (int attempt = 0; attempt < 5; attempt++) {
            String candidate = shareIdGenerator.next();
            if (!deckRepository.existsByShareId(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Could not find a free share id");
    }

    private Deck findOwnedDeck(User user, UUID id) {
        return deckRepository
                .findByIdAndOwnerUserIdAndDeletedAtIsNull(id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Deck not found: " + id));
    }

    private DeckDto toDto(Deck deck) {
        return new DeckDto(
                deck.getId(),
                deck.getTitle(),
                deck.getLanguage(),
                deck.getShareId(),
                deck.isShareEnabled(),
                deck.getItems().stream().map(item -> item.getItem().getId()).toList(),
                deck.getCreatedAt(),
                deck.getUpdatedAt());
    }
}

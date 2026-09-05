package com.almonium.card.deck.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.repository.ExampleRepository;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.repository.TranslationRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.card.deck.dto.response.AddedWordsResult;
import com.almonium.card.deck.dto.response.SharedCardView;
import com.almonium.card.deck.dto.response.SharedDeckView;
import com.almonium.card.deck.dto.response.SharedLinkViewerStatus;
import com.almonium.card.deck.dto.response.SharerDto;
import com.almonium.card.deck.model.entity.Deck;
import com.almonium.card.deck.model.entity.DeckItem;
import com.almonium.card.deck.model.enums.SharedLinkStatus;
import com.almonium.card.deck.repository.DeckRepository;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * A card or a deck opened from a link. The public read never depends on who is looking; a signed-in viewer asks
 * separately what they already hold, and adding copies the words into their own account from that moment on. Later
 * edits by the owner never reach a copy.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SharedLinkService {
    DeckRepository deckRepository;
    LearningItemRepository learningItemRepository;
    TranslationRepository translationRepository;
    ExampleRepository exampleRepository;
    LearnerRepository learnerRepository;
    LearnerFinder learnerFinder;
    EffectiveAccessService effectiveAccessService;
    SharedWordMapper sharedWordMapper;

    public SharedCardView viewCard(UUID publicId) {
        LearningItem item = findSharedCard(publicId);
        return new SharedCardView(item.getLanguage(), sharedWordMapper.toShared(item), sharerOf(item.getOwner()));
    }

    public SharedDeckView viewDeck(String shareId) {
        Deck deck = findDeck(shareId);
        SharedLinkStatus status = statusOf(deck);
        if (status != SharedLinkStatus.ACTIVE) {
            return SharedDeckView.dead(status);
        }
        return new SharedDeckView(
                status,
                deck.getShareId(),
                deck.getTitle(),
                deck.getLanguage(),
                deck.getItems().stream()
                        .map(DeckItem::getItem)
                        .map(sharedWordMapper::toShared)
                        .toList(),
                sharerOf(deck.getOwner()));
    }

    public SharedLinkViewerStatus cardViewer(User viewer, UUID publicId) {
        LearningItem item = findSharedCard(publicId);
        return viewerStatus(viewer, item.getOwner(), item.getLanguage(), List.of(item));
    }

    public SharedLinkViewerStatus deckViewer(User viewer, String shareId) {
        Deck deck = findDeck(shareId);
        List<LearningItem> words =
                deck.getItems().stream().map(DeckItem::getItem).toList();
        return viewerStatus(viewer, deck.getOwner(), deck.getLanguage(), words);
    }

    @Transactional
    public AddedWordsResult addCard(User viewer, UUID publicId) {
        LearningItem item = findSharedCard(publicId);
        return addWords(viewer, item.getLanguage(), List.of(item));
    }

    @Transactional
    public AddedWordsResult addFromDeck(User viewer, String shareId, Collection<UUID> wordIds) {
        Deck deck = findDeck(shareId);
        if (statusOf(deck) != SharedLinkStatus.ACTIVE) {
            throw new BadUserRequestActionException("This link no longer works.");
        }
        Set<UUID> requested = Set.copyOf(wordIds);
        List<LearningItem> words = deck.getItems().stream()
                .map(DeckItem::getItem)
                .filter(item -> requested.contains(item.getId()))
                .toList();
        return addWords(viewer, deck.getLanguage(), words);
    }

    private AddedWordsResult addWords(User viewer, Language language, List<LearningItem> words) {
        Learner learner = learnerRepository
                .findByUserIdAndLanguage(viewer.getId(), language)
                .orElseThrow(() -> new BadUserRequestActionException(
                        "Add " + language + " to your languages before keeping these words."));
        learnerFinder.requireActive(learner);
        Map<String, LearningItem> held = heldByForm(learner, words);
        int added = 0;
        int alreadyHeld = 0;
        Instant firstDueAt = null;
        for (LearningItem word : words) {
            if (held.containsKey(word.getNormalizedForm())) {
                alreadyHeld++;
                continue;
            }
            LearningItem copy = sharedWordMapper.copyFor(learner, word);
            Instant now = Instant.now();
            copy.setCreatedAt(now);
            copy.setUpdatedAt(now);
            copy.setDueAt(now);
            learningItemRepository.save(copy);
            translationRepository.saveAll(copy.getTranslations());
            exampleRepository.saveAll(copy.getExamples());
            held.put(copy.getNormalizedForm(), copy);
            added++;
            if (firstDueAt == null) {
                firstDueAt = copy.getDueAt();
            }
        }
        log.info("Copied {} shared words into learner {} ({} already held)", added, learner.getId(), alreadyHeld);
        return new AddedWordsResult(added, alreadyHeld, firstDueAt);
    }

    private SharedLinkViewerStatus viewerStatus(
            User viewer, Learner owner, Language language, List<LearningItem> words) {
        boolean isOwner = owner.getUser().getId().equals(viewer.getId());
        Optional<Learner> learner = learnerRepository.findByUserIdAndLanguage(viewer.getId(), language);
        if (learner.isEmpty()) {
            return new SharedLinkViewerStatus(isOwner, false, List.of(), 0);
        }
        Map<String, LearningItem> held = heldByForm(learner.get(), words);
        List<UUID> heldIds = words.stream()
                .filter(word -> held.containsKey(word.getNormalizedForm()))
                .map(LearningItem::getId)
                .toList();
        Instant now = Instant.now();
        int due = (int) words.stream()
                .map(word -> held.get(word.getNormalizedForm()))
                .filter(mine -> mine != null
                        && mine.getDueAt() != null
                        && !mine.getDueAt().isAfter(now))
                .count();
        return new SharedLinkViewerStatus(isOwner, true, heldIds, due);
    }

    private Map<String, LearningItem> heldByForm(Learner learner, List<LearningItem> words) {
        Set<String> forms = words.stream().map(LearningItem::getNormalizedForm).collect(Collectors.toSet());
        if (forms.isEmpty()) {
            return new java.util.HashMap<>();
        }
        return learningItemRepository.findAllByOwnerAndNormalizedFormIn(learner, forms).stream()
                .collect(Collectors.toMap(
                        LearningItem::getNormalizedForm, Function.identity(), (a, b) -> a, java.util.HashMap::new));
    }

    private SharedLinkStatus statusOf(Deck deck) {
        if (deck.isDeleted()) {
            return SharedLinkStatus.DELETED;
        }
        return deck.isShareEnabled() ? SharedLinkStatus.ACTIVE : SharedLinkStatus.REVOKED;
    }

    private SharerDto sharerOf(Learner owner) {
        User user = owner.getUser();
        String avatarUrl = user.getProfile() == null ? null : user.getProfile().getAvatarUrl();
        return new SharerDto(user.getUsername(), avatarUrl, effectiveAccessService.isPremium(user));
    }

    private Deck findDeck(String shareId) {
        return deckRepository
                .findByShareId(shareId)
                .orElseThrow(() -> new EntityNotFoundException("No deck behind link " + shareId));
    }

    private LearningItem findSharedCard(UUID publicId) {
        return learningItemRepository
                .getByPublicId(publicId)
                .orElseThrow(() -> new EntityNotFoundException("No card behind link " + publicId));
    }
}

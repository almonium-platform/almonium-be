package com.almonium.learning.review.service;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.Example;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.model.entity.Translation;
import com.almonium.card.core.model.enums.LearningIntent;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.learning.review.dto.ConfusedItemResponse;
import com.almonium.learning.review.dto.LeechItemResponse;
import com.almonium.learning.review.dto.ReviewAnswerRequest;
import com.almonium.learning.review.dto.ReviewAnswerResponse;
import com.almonium.learning.review.dto.ReviewHintResponse;
import com.almonium.learning.review.dto.ReviewItemResponse;
import com.almonium.learning.review.dto.ReviewSessionResponse;
import com.almonium.learning.review.dto.ReviewSessionResultResponse;
import com.almonium.learning.review.dto.ReviewSummaryResponse;
import com.almonium.learning.review.model.ConfusionEdge;
import com.almonium.learning.review.model.ReviewEvent;
import com.almonium.learning.review.model.ReviewOutcome;
import com.almonium.learning.review.model.ReviewPrompt;
import com.almonium.learning.review.model.ReviewPromptType;
import com.almonium.learning.review.model.ReviewSession;
import com.almonium.learning.review.model.ReviewSessionItem;
import com.almonium.learning.review.repository.ConfusionEdgeRepository;
import com.almonium.learning.review.repository.ReviewEventRepository;
import com.almonium.learning.review.repository.ReviewPromptRepository;
import com.almonium.learning.review.repository.ReviewSessionItemRepository;
import com.almonium.learning.review.repository.ReviewSessionRepository;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import io.github.openspacedrepetition.Card;
import io.github.openspacedrepetition.CardAndReviewLog;
import io.github.openspacedrepetition.Rating;
import io.github.openspacedrepetition.Scheduler;
import jakarta.persistence.EntityNotFoundException;
import java.text.Normalizer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private static final int SESSION_SIZE = 10;
    private static final int LEECH_THRESHOLD = 4;

    private final LearnerFinder learnerFinder;
    private final LearningItemRepository learningItemRepository;
    private final ReviewPromptRepository promptRepository;
    private final ReviewEventRepository eventRepository;
    private final ConfusionEdgeRepository confusionEdgeRepository;
    private final ReviewSessionRepository sessionRepository;
    private final ReviewSessionItemRepository sessionItemRepository;
    private final Scheduler scheduler = Scheduler.builder().enableFuzzing(false).build();

    public ReviewSummaryResponse getSummary(User user, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        Instant now = Instant.now();
        List<LearningItem> due =
                learningItemRepository.findAllByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(learner, now);
        List<LearningItem> sessionItems = due.stream().limit(SESSION_SIZE).toList();
        long understand = sessionItems.stream()
                .filter(item -> chooseIntent(item) == LearningIntent.UNDERSTAND)
                .count();
        long produce = sessionItems.stream()
                .filter(item -> chooseIntent(item) == LearningIntent.PRODUCE)
                .count();
        long disambiguate = sessionItems.stream()
                .filter(item -> chooseIntent(item) == LearningIntent.DISAMBIGUATE)
                .count();
        List<LearningItem> leeches = learningItemRepository.findAllByOwnerAndLeechTrueOrderByUpdatedAtAsc(learner);
        return new ReviewSummaryResponse(
                due.size(),
                Math.min(SESSION_SIZE, due.size()),
                understand,
                produce,
                disambiguate,
                leeches.size(),
                leeches.stream()
                        .map(item -> new LeechItemResponse(
                                item.getId(),
                                item.getEntry(),
                                item.getFailurePromptType(),
                                item.getConsecutiveFailures()))
                        .toList());
    }

    @Transactional
    public void reencounter(User user, UUID itemId) {
        LearningItem item = learningItemRepository
                .findByIdAndOwnerUserId(itemId, user.getId())
                .filter(LearningItem::isLeech)
                .orElseThrow(() -> new EntityNotFoundException("Leech learning item not found"));
        LearningIntent alternate = ReviewPromptType.FORM_RECALL.name().equals(item.getFailurePromptType())
                ? LearningIntent.UNDERSTAND
                : LearningIntent.PRODUCE;
        item.getLearningIntents().add(alternate);
        if (promptRepository
                .findAllByLearningItemAndIntentAndActiveTrueOrderByPosition(item, alternate)
                .isEmpty()) {
            createPrompts(item, alternate);
        }
        item.setLeech(false);
        item.setConsecutiveFailures(0);
        item.setFailurePromptType(null);
        item.setDueAt(Instant.now());
        learningItemRepository.save(item);
    }

    @Transactional
    public ReviewSessionResponse startSession(User user, Language language) {
        Learner learner = learnerFinder.findLearner(user, language);
        Instant now = Instant.now();
        List<LearningItem> due =
                learningItemRepository.findTop10ByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(learner, now);
        ReviewSession session = sessionRepository.save(ReviewSession.builder()
                .owner(user)
                .language(language)
                .createdAt(now)
                .build());
        List<ReviewItemResponse> responses = new ArrayList<>();
        for (int index = 0; index < due.size(); index++) {
            LearningItem item = due.get(index);
            sessionItemRepository.save(ReviewSessionItem.builder()
                    .session(session)
                    .learningItem(item)
                    .position(index)
                    .build());
            responses.add(toReviewItem(item));
        }
        long backlog = learningItemRepository.countByOwnerAndLeechFalseAndDueAtLessThanEqual(learner, now);
        return new ReviewSessionResponse(session.getId(), Math.toIntExact(backlog), responses);
    }

    @Transactional
    public ReviewAnswerResponse answer(User user, UUID sessionId, UUID itemId, ReviewAnswerRequest request) {
        ReviewSession session = ownedSession(user, sessionId);
        ReviewSessionItem sessionItem = sessionItemRepository
                .findBySessionIdAndLearningItemId(sessionId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Learning item is not in this review session"));
        if (sessionItem.getCompletedAt() != null) {
            throw new IllegalStateException("Learning item has already been answered in this session");
        }
        LearningItem item = sessionItem.getLearningItem();
        ReviewPrompt prompt = promptRepository
                .findById(request.promptId())
                .filter(candidate -> candidate.getLearningItem().getId().equals(itemId))
                .orElseThrow(() -> new EntityNotFoundException("Review prompt not found for learning item"));

        List<String> hints = Optional.ofNullable(request.hintsOpened()).orElseGet(List::of);
        boolean exact = exactAnswerMatches(request.answer(), prompt.getExpectedAnswer());
        LearningItem confusedWith = !request.revealed() && !exact
                ? findConfusedItem(item, request.answer()).orElse(null)
                : null;
        boolean correct = !request.revealed()
                && confusedWith == null
                && answerMatches(request.answer(), prompt.getExpectedAnswer());
        ReviewOutcome outcome = determineOutcome(correct, request.revealed(), hints, confusedWith);
        Rating rating =
                switch (outcome) {
                    case CORRECT -> Rating.GOOD;
                    case RECOVERED_WITH_HINT -> Rating.HARD;
                    case INCORRECT, CONFUSED, TYPO_CORRECTION -> Rating.AGAIN;
                };

        Instant reviewedAt = Instant.now();
        Card before = fsrsCard(item);
        String beforeJson = before.toJson();
        Instant dueBefore = item.getDueAt();
        CardAndReviewLog scheduled = scheduler.reviewCard(before, rating, reviewedAt);
        item.setFsrsCardJson(scheduled.card().toJson());
        item.setDueAt(scheduled.card().getDue());
        item.setLastReviewedAt(reviewedAt);
        item.setTotalReviews(item.getTotalReviews() + 1);
        updateLeechState(item, prompt.getPromptType(), correct);
        learningItemRepository.save(item);

        if (confusedWith != null) {
            coScheduleConfusedItem(confusedWith, item.getDueAt());
        }
        ConfusionEdge edge = confusedWith == null ? null : recordConfusion(user, item, confusedWith, reviewedAt);
        UUID eventId = UUID.randomUUID();
        ReviewEvent event = eventRepository.save(ReviewEvent.builder()
                .id(eventId)
                .reviewedAt(reviewedAt)
                .learningItem(item)
                .owner(user)
                .session(session)
                .intent(prompt.getIntent())
                .promptType(prompt.getPromptType())
                .prompt(prompt)
                .answer(request.answer())
                .expectedAnswer(prompt.getExpectedAnswer())
                .outcome(outcome)
                .hintsOpened(String.join(",", hints))
                .confusedWithItem(confusedWith)
                .fsrsCardBefore(beforeJson)
                .fsrsCardAfter(scheduled.card().toJson())
                .dueBefore(dueBefore)
                .dueAfter(item.getDueAt())
                .build());

        sessionItem.setCompletedAt(reviewedAt);
        sessionItem.setReviewEventId(event.getId());
        sessionItemRepository.save(sessionItem);
        int completed = Math.toIntExact(sessionItemRepository.countBySessionIdAndCompletedAtIsNotNull(sessionId));
        int total = sessionItemRepository
                .findAllBySessionIdOrderByPosition(sessionId)
                .size();
        if (completed == total) {
            session.setCompletedAt(reviewedAt);
            sessionRepository.save(session);
        }
        return new ReviewAnswerResponse(
                eventId,
                outcome,
                request.answer(),
                prompt.getExpectedAnswer(),
                confusedWith == null ? null : confusionResponse(confusedWith, edge),
                item.getDueAt(),
                item.isLeech(),
                completed,
                total);
    }

    @Transactional
    public void markMistype(User user, UUID eventId) {
        if (eventRepository.existsByCorrectsEventIdAndOwnerId(eventId, user.getId())) {
            return;
        }
        ReviewEvent original = eventRepository
                .findTopByIdAndOwnerIdOrderByReviewedAtDesc(eventId, user.getId())
                .filter(event -> event.getOutcome() == ReviewOutcome.CONFUSED)
                .orElseThrow(() -> new EntityNotFoundException("Confusion review event not found"));
        LearningItem confusedWith = original.getConfusedWithItem();
        confusionEdgeRepository
                .findByOwnerAndSourceItemAndTargetItem(user, original.getLearningItem(), confusedWith)
                .ifPresent(edge -> {
                    edge.setResolvedCount(edge.getResolvedCount() + 1);
                    confusionEdgeRepository.save(edge);
                });
        Instant now = Instant.now();
        eventRepository.save(ReviewEvent.builder()
                .id(UUID.randomUUID())
                .reviewedAt(now)
                .learningItem(original.getLearningItem())
                .owner(user)
                .session(original.getSession())
                .intent(original.getIntent())
                .promptType(original.getPromptType())
                .prompt(original.getPrompt())
                .answer(original.getAnswer())
                .expectedAnswer(original.getExpectedAnswer())
                .outcome(ReviewOutcome.TYPO_CORRECTION)
                .hintsOpened("")
                .confusedWithItem(confusedWith)
                .correctsEventId(original.getId())
                .fsrsCardBefore(original.getFsrsCardAfter())
                .fsrsCardAfter(original.getFsrsCardAfter())
                .dueBefore(original.getDueAfter())
                .dueAfter(original.getDueAfter())
                .build());
    }

    public ReviewSessionResultResponse sessionResult(User user, UUID sessionId) {
        ReviewSession session = ownedSession(user, sessionId);
        List<ReviewEvent> events = eventRepository.findAllBySessionIdOrderByReviewedAt(sessionId).stream()
                .filter(event -> event.getOutcome() != ReviewOutcome.TYPO_CORRECTION)
                .toList();
        int straight = Math.toIntExact(events.stream()
                .filter(e -> e.getOutcome() == ReviewOutcome.CORRECT)
                .count());
        int hinted = Math.toIntExact(events.stream()
                .filter(e -> e.getOutcome() == ReviewOutcome.RECOVERED_WITH_HINT)
                .count());
        int confused = Math.toIntExact(events.stream()
                .filter(e -> e.getOutcome() == ReviewOutcome.CONFUSED)
                .count());
        Learner learner = learnerFinder.findLearner(user, session.getLanguage());
        long stillDue = learningItemRepository.countByOwnerAndLeechFalseAndDueAtLessThanEqual(learner, Instant.now());
        List<String> dessert = events.stream()
                .filter(event ->
                        event.getOutcome() == ReviewOutcome.INCORRECT || event.getOutcome() == ReviewOutcome.CONFUSED)
                .map(ReviewEvent::getLearningItem)
                .distinct()
                .map(this::dessertSentence)
                .filter(sentence -> !sentence.isBlank())
                .limit(4)
                .toList();
        return new ReviewSessionResultResponse(events.size(), straight, hinted, confused, stillDue, dessert);
    }

    private ReviewSession ownedSession(User user, UUID sessionId) {
        return sessionRepository
                .findByIdAndOwnerId(sessionId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Review session not found"));
    }

    private ReviewItemResponse toReviewItem(LearningItem item) {
        LearningIntent intent = chooseIntent(item);
        List<ReviewPrompt> prompts =
                promptRepository.findAllByLearningItemAndIntentAndActiveTrueOrderByPosition(item, intent);
        if (prompts.isEmpty()) {
            prompts = createPrompts(item, intent);
        }
        ReviewPrompt prompt = prompts.get(Math.floorMod(item.getTotalReviews(), prompts.size()));
        return new ReviewItemResponse(
                item.getId(),
                prompt.getId(),
                intent,
                prompt.getPromptType(),
                prompt.getPromptText(),
                item.getSourceContext(),
                item.getLanguage().name(),
                item.getCreatedAt(),
                item.getTotalReviews(),
                hints(item, prompt));
    }

    private LearningIntent chooseIntent(LearningItem item) {
        Set<LearningIntent> intents = item.getLearningIntents();
        if (intents == null || intents.isEmpty()) {
            return LearningIntent.UNDERSTAND;
        }
        List<LearningIntent> ordered = intents.stream()
                .filter(intent -> intent != LearningIntent.PRONOUNCE)
                .sorted(Comparator.comparingInt(Enum::ordinal))
                .toList();
        if (ordered.isEmpty()) {
            return LearningIntent.UNDERSTAND;
        }
        return ordered.get(Math.floorMod(item.getTotalReviews(), ordered.size()));
    }

    private List<ReviewPrompt> createPrompts(LearningItem item, LearningIntent intent) {
        List<String> meanings = item.getTranslations().stream()
                .map(Translation::getTranslation)
                .filter(value -> value != null && !value.isBlank())
                .distinct()
                .toList();
        if (meanings.isEmpty()) {
            meanings = List.of(Optional.ofNullable(item.getSelectedSense()).orElse(item.getEntry()));
        }
        List<ReviewPrompt> prompts = new ArrayList<>();
        for (int index = 0; index < meanings.size(); index++) {
            String meaning = meanings.get(index);
            boolean receptive = intent == LearningIntent.UNDERSTAND;
            ReviewPromptType type = intent == LearningIntent.DISAMBIGUATE
                    ? ReviewPromptType.SENSE_DISCRIMINATION
                    : receptive ? ReviewPromptType.MEANING_RECALL : ReviewPromptType.FORM_RECALL;
            prompts.add(promptRepository.save(ReviewPrompt.builder()
                    .learningItem(item)
                    .intent(intent)
                    .promptType(type)
                    .promptText(receptive ? item.getEntry() : meaning)
                    .expectedAnswer(receptive ? meaning : item.getEntry())
                    .position(index)
                    .build()));
        }
        return prompts;
    }

    private List<ReviewHintResponse> hints(LearningItem item, ReviewPrompt prompt) {
        String answer = prompt.getExpectedAnswer();
        String gender = Arrays.stream(new String[] {"der", "die", "das"})
                .filter(article -> normalize(answer).startsWith(article + " "))
                .findFirst()
                .orElse("No grammatical gender is stored");
        String letters = answer.length() <= 2 ? answer : answer.substring(0, Math.min(3, answer.length())) + "…";
        return List.of(
                new ReviewHintResponse("GENDER", "The gender", "Costs a little", gender),
                new ReviewHintResponse("LETTERS", "Two more letters", "Costs more", letters),
                new ReviewHintResponse(
                        "SOURCE_CONTEXT",
                        "The sentence you saved it from",
                        "Counts as a miss you recovered",
                        Optional.ofNullable(item.getSourceContext()).orElse("No source sentence was saved")));
    }

    private ReviewOutcome determineOutcome(
            boolean correct, boolean revealed, List<String> hints, LearningItem confusedWith) {
        if (correct) {
            return hints.isEmpty() ? ReviewOutcome.CORRECT : ReviewOutcome.RECOVERED_WITH_HINT;
        }
        if (confusedWith != null && !revealed) {
            return ReviewOutcome.CONFUSED;
        }
        return ReviewOutcome.INCORRECT;
    }

    private boolean answerMatches(String submitted, String expected) {
        String actual = normalize(submitted);
        return Arrays.stream(expected.split("\\|"))
                .map(this::normalize)
                .anyMatch(candidate ->
                        candidate.equals(actual) || (candidate.length() >= 5 && levenshtein(candidate, actual) <= 1));
    }

    private boolean exactAnswerMatches(String submitted, String expected) {
        String actual = normalize(submitted);
        return Arrays.stream(expected.split("\\|")).map(this::normalize).anyMatch(actual::equals);
    }

    private Optional<LearningItem> findConfusedItem(LearningItem asked, String answer) {
        String normalized = normalize(answer);
        return learningItemRepository.findAllByOwner(asked.getOwner()).stream()
                .filter(item -> !item.getId().equals(asked.getId()))
                .filter(item -> normalize(item.getEntry()).equals(normalized)
                        || item.getTranslations().stream()
                                .map(Translation::getTranslation)
                                .map(this::normalize)
                                .anyMatch(normalized::equals))
                .findFirst();
    }

    private ConfusionEdge recordConfusion(User user, LearningItem source, LearningItem target, Instant now) {
        ConfusionEdge edge = confusionEdgeRepository
                .findByOwnerAndSourceItemAndTargetItem(user, source, target)
                .map(existing -> {
                    existing.setConfusionCount(existing.getConfusionCount() + 1);
                    existing.setLastConfusedAt(now);
                    return existing;
                })
                .orElseGet(() -> ConfusionEdge.builder()
                        .owner(user)
                        .sourceItem(source)
                        .targetItem(target)
                        .lastConfusedAt(now)
                        .build());
        return confusionEdgeRepository.save(edge);
    }

    private void coScheduleConfusedItem(LearningItem confusedItem, Instant pairDueAt) {
        if (confusedItem.getDueAt() != null && confusedItem.getDueAt().isBefore(pairDueAt)) {
            return;
        }
        Card memory = fsrsCard(confusedItem);
        memory.setDue(pairDueAt);
        confusedItem.setDueAt(pairDueAt);
        confusedItem.setFsrsCardJson(memory.toJson());
        learningItemRepository.save(confusedItem);
    }

    private ConfusedItemResponse confusionResponse(LearningItem item, ConfusionEdge edge) {
        String meaning = item.getTranslations().stream()
                .map(Translation::getTranslation)
                .findFirst()
                .orElse(item.getSelectedSense());
        String example =
                item.getExamples().stream().map(Example::getExample).findFirst().orElse(null);
        return new ConfusedItemResponse(item.getId(), item.getEntry(), meaning, example, edge.getConfusionCount());
    }

    private Card fsrsCard(LearningItem item) {
        if (item.getFsrsCardJson() != null && !item.getFsrsCardJson().isBlank()) {
            return Card.fromJson(item.getFsrsCardJson());
        }
        return Card.builder().due(item.getDueAt()).build();
    }

    private void updateLeechState(LearningItem item, ReviewPromptType promptType, boolean correct) {
        if (correct) {
            item.setConsecutiveFailures(0);
            item.setFailurePromptType(null);
            return;
        }
        String shape = promptType.name();
        int failures = shape.equals(item.getFailurePromptType()) ? item.getConsecutiveFailures() + 1 : 1;
        item.setFailurePromptType(shape);
        item.setConsecutiveFailures(failures);
        item.setLeech(failures >= LEECH_THRESHOLD);
    }

    private String dessertSentence(LearningItem item) {
        return item.getExamples().stream()
                .map(Example::getExample)
                .filter(value -> value != null && !value.isBlank())
                .findFirst()
                .orElse("");
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }
        return Normalizer.normalize(value, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT)
                .replaceAll("[\\p{Punct}\\s]+", " ")
                .trim();
    }

    private int levenshtein(String left, String right) {
        int[] previous = new int[right.length() + 1];
        for (int index = 0; index <= right.length(); index++) {
            previous[index] = index;
        }
        for (int leftIndex = 1; leftIndex <= left.length(); leftIndex++) {
            int[] current = new int[right.length() + 1];
            current[0] = leftIndex;
            for (int rightIndex = 1; rightIndex <= right.length(); rightIndex++) {
                int cost = left.charAt(leftIndex - 1) == right.charAt(rightIndex - 1) ? 0 : 1;
                current[rightIndex] = Math.min(
                        Math.min(current[rightIndex - 1] + 1, previous[rightIndex] + 1),
                        previous[rightIndex - 1] + cost);
            }
            previous = current;
        }
        return previous[right.length()];
    }
}

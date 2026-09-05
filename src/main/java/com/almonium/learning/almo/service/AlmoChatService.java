package com.almonium.learning.almo.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.card.core.model.entity.LearningItem;
import com.almonium.card.core.repository.LearningItemRepository;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.config.properties.AlmoProperties;
import com.almonium.learning.almo.client.OpenAiChatClient;
import com.almonium.learning.almo.dto.AlmoChatDto;
import com.almonium.learning.almo.dto.AlmoReplyDto;
import com.almonium.learning.almo.model.AlmoTurn;
import com.almonium.learning.almo.repository.AlmoQueueRepository;
import com.almonium.learning.almo.repository.AlmoTurnRepository;
import com.almonium.learning.review.model.ConfusionEdge;
import com.almonium.learning.review.repository.ConfusionEdgeRepository;
import com.almonium.learning.review.service.ReviewService;
import com.almonium.subscription.service.EffectiveAccessService;
import com.almonium.user.core.exception.ResourceNotAccessibleException;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.LearnerRepository;
import io.getstream.chat.java.models.Message;
import jakarta.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * 11: chat with Almo. Premium, one channel per target language, and a reply generated on request from the words in
 * the learner's queue. The clients own the sending; this owns the answering, the ceiling and the evidence.
 *
 * <p>No class-level transaction: a turn holds an HTTP call to the model for seconds, and a database connection has no
 * business waiting on it. The saves that need a transaction get one of their own.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class AlmoChatService {
    static final String WORDS_FIELD = "almoWords";
    static final String CONTRAST_FIELD = "almoContrast";
    static final String TRANSLATION_FIELD = "almoTranslation";
    private static final String ASSISTANT = "assistant";
    private static final String LEARNER = "user";

    EffectiveAccessService effectiveAccessService;
    LearnerRepository learnerRepository;
    LearnerFinder learnerFinder;
    LearningItemRepository learningItemRepository;
    AlmoQueueRepository queueRepository;
    ConfusionEdgeRepository confusionEdgeRepository;
    AlmoTurnRepository turnRepository;
    AlmoStreamGateway stream;
    OpenAiChatClient model;
    AlmoPromptBuilder prompts;
    AlmoLanguageCopy copy;
    ReviewService reviewService;
    AlmoProperties properties;
    ObjectMapper objectMapper;

    /**
     * One channel per active target language, created on demand and idempotent, so a client asks on every load and a
     * new language or a new plan needs no event of its own. A free account gets an empty list and no channel.
     */
    public List<AlmoChatDto> ensureChats(User user) {
        if (!effectiveAccessService.isPremium(user)) {
            return List.of();
        }

        stream.ensureAlmoUser();
        return learnerRepository.findAllByUserIdOrderByLanguage(user.getId()).stream()
                .filter(Learner::isActive)
                .map(learner -> {
                    Language language = learner.getLanguage();
                    String cid = stream.ensureChannel(user.getId(), language, copy.channelName(language));
                    Optional<String> word =
                            queue(learner).stream().map(LearningItem::getEntry).findFirst();
                    return new AlmoChatDto(
                            cid,
                            language,
                            copy.channelName(language),
                            copy.placeholder(language),
                            copy.openers(language, word));
                })
                .toList();
    }

    /**
     * Answers the learner's message. Keyed by that message, so two clients asking for the same reply buy one; capped
     * per rolling day, silently, because the ceiling is a cost control and not a feature.
     */
    public AlmoReplyDto reply(User user, Language language, String userMessageId) {
        if (!effectiveAccessService.isPremium(user)) {
            throw new ResourceNotAccessibleException("Almo talks with members only");
        }
        if (!model.isConfigured()) {
            throw new ApiIntegrationException("Almo has no model to speak with on this server");
        }

        Optional<AlmoTurn> answered = turnRepository.findByUserMessageId(userMessageId);
        if (answered.isPresent()) {
            return new AlmoReplyDto(answered.get().getReplyMessageId(), false);
        }

        Instant now = Instant.now();
        long today = turnRepository.countByUserIdAndCreatedAtAfter(user.getId(), now.minus(Duration.ofDays(1)));
        if (today >= properties.getDailyMessageCeiling()) {
            log.info("Almo's daily ceiling reached for user {} in {}", user.getId(), language);
            return new AlmoReplyDto(null, true);
        }

        Learner learner = learnerFinder.findLearner(user, language);
        String channelId = stream.channelId(user.getId(), language);
        List<OpenAiChatClient.Turn> history =
                history(stream.recentMessages(channelId, properties.getHistorySize()), userMessageId);

        List<LearningItem> queue = queue(learner);
        List<ConfusionEdge> confusions = confusionEdgeRepository.findAllByOwnerAndSourceItemLanguage(user, language);
        String system = prompts.system(language, learner.getSelfReportedLevel(), queue, confusions);

        stream.typing(channelId, true);
        OpenAiChatClient.Completion completion;
        AlmoDraft draft;
        try {
            completion = model.complete(system, history);
            draft = parse(completion.content());
        } catch (RuntimeException e) {
            stream.typing(channelId, false);
            throw e;
        }

        if (draft.reply().isBlank()) {
            stream.typing(channelId, false);
            throw new ApiIntegrationException("The model answered with an empty reply");
        }

        String replyId = stream.sendReply(channelId, draft.reply(), replyFields(draft));
        stream.typing(channelId, false);

        List<String> learnerSurfaces = draft.learnerWords().stream()
                .map(AlmoDraft.WordUse::surface)
                .distinct()
                .toList();
        if (!learnerSurfaces.isEmpty()) {
            stream.annotateMessage(userMessageId, user.getId(), Map.of(WORDS_FIELD, learnerSurfaces));
        }

        recordEvidence(user, queue, draft);

        turnRepository.save(AlmoTurn.builder()
                .userId(user.getId())
                .language(language)
                .userMessageId(userMessageId)
                .replyMessageId(replyId)
                .model(completion.model())
                .promptTokens(completion.promptTokens())
                .completionTokens(completion.completionTokens())
                .createdAt(now)
                .build());

        return new AlmoReplyDto(replyId, false);
    }

    /** Due words first, then the most recently saved, up to the prompt's cap. Never the whole deck. */
    private List<LearningItem> queue(Learner learner) {
        int cap = properties.getPromptWords();
        Map<java.util.UUID, LearningItem> picked = new LinkedHashMap<>();
        learningItemRepository
                .findAllByOwnerAndLeechFalseAndDueAtLessThanEqualOrderByDueAtAsc(learner, Instant.now())
                .stream()
                .limit(cap)
                .forEach(item -> picked.put(item.getId(), item));
        if (picked.size() < cap) {
            queueRepository
                    .findAllByOwnerAndLeechFalseOrderByCreatedAtDesc(learner, PageRequest.of(0, cap))
                    .forEach(item -> {
                        if (picked.size() < cap) {
                            picked.putIfAbsent(item.getId(), item);
                        }
                    });
        }
        return new ArrayList<>(picked.values());
    }

    /** The thread up to and including the message being answered; anything the learner typed after it waits its turn. */
    private List<OpenAiChatClient.Turn> history(List<Message> messages, String userMessageId) {
        List<OpenAiChatClient.Turn> turns = new ArrayList<>();
        boolean found = false;
        for (Message message : messages) {
            String text = message.getText();
            if (text != null && !text.isBlank()) {
                var author = message.getUser();
                boolean fromAlmo = author != null && stream.almoUserId().equals(author.getId());
                turns.add(new OpenAiChatClient.Turn(fromAlmo ? ASSISTANT : LEARNER, text.trim()));
            }
            if (userMessageId.equals(message.getId())) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new EntityNotFoundException("That message is not in this chat");
        }
        return turns;
    }

    private AlmoDraft parse(String content) {
        try {
            return objectMapper.readValue(content, AlmoDraft.class).normalized();
        } catch (JacksonException e) {
            throw new ApiIntegrationException("The model answered with something other than a turn", e);
        }
    }

    private Map<String, Object> replyFields(AlmoDraft draft) {
        Map<String, Object> fields = new HashMap<>();
        List<String> words = draft.replyWords().stream()
                .map(AlmoDraft.WordUse::surface)
                .distinct()
                .toList();
        if (!words.isEmpty()) {
            fields.put(WORDS_FIELD, words);
        }
        if (!draft.contrast().isEmpty()) {
            fields.put(CONTRAST_FIELD, draft.contrast());
        }
        if (!draft.translation().isBlank()) {
            fields.put(TRANSLATION_FIELD, draft.translation());
        }
        return fields;
    }

    /**
     * Production is evidence. A queue word the learner wrote correctly is retrieval without a prompt, and pushes its
     * next review further out than a review pass would; a misused member of a confusion pair writes the pair down.
     * The scheduler update is the half that matters; the client's dotted line is only the visible half.
     */
    private void recordEvidence(User user, List<LearningItem> queue, AlmoDraft draft) {
        Map<String, LearningItem> byEntry = new HashMap<>();
        queue.forEach(item -> byEntry.putIfAbsent(key(item.getEntry()), item));

        List<LearningItem> produced = draft.learnerWords().stream()
                .map(use -> byEntry.get(key(use.entry())))
                .filter(item -> item != null)
                .distinct()
                .toList();

        AlmoDraft.Confusion confusion = draft.confusion();
        LearningItem intended = confusion == null ? null : byEntry.get(key(confusion.intended()));
        LearningItem misused = confusion == null ? null : byEntry.get(key(confusion.misused()));
        if (intended != null && misused != null && !intended.getId().equals(misused.getId())) {
            produced = produced.stream()
                    .filter(item -> !item.getId().equals(intended.getId()))
                    .toList();
        }

        try {
            if (!produced.isEmpty()) {
                reviewService.recordProduction(produced);
            }
            if (intended != null && misused != null && !intended.getId().equals(misused.getId())) {
                reviewService.recordMisuse(user, intended, misused);
            }
        } catch (RuntimeException e) {
            // The reply is already on screen; losing one scheduler update is cheaper than a failed request for it.
            log.error("Could not record Almo's evidence for user {}: {}", user.getId(), e.getMessage(), e);
        }
    }

    private static String key(String entry) {
        return entry == null ? "" : entry.trim().toLowerCase(Locale.ROOT);
    }
}

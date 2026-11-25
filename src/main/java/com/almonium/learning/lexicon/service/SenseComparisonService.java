package com.almonium.learning.lexicon.service;

import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.SenseComparisonProperties;
import com.almonium.learning.almo.client.OpenAiChatClient;
import com.almonium.learning.lexicon.dto.request.SenseComparisonRequest;
import com.almonium.learning.lexicon.dto.response.SenseComparisonResponse;
import com.almonium.learning.lexicon.repository.SenseComparisonStore;
import com.almonium.learning.lexicon.repository.SenseComparisonStore.SenseFacts;
import com.almonium.user.core.exception.BadUserRequestActionException;
import com.almonium.user.core.model.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

/**
 * Discover's "word vs word": how two senses differ, in a few sentences a model writes from the lexicon's facts for
 * the two and nothing else. An explanation is bought once per content (the prompt, the language it is written in
 * and the facts sent), whoever asks and in whichever order the words were typed; new ones are capped per member per
 * rolling day, because the ceiling is a cost control.
 *
 * <p>No class-level transaction: the request holds an HTTP call to the model for seconds.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SenseComparisonService {
    /** Part of every explanation's identity: raise it when the prompt changes what is worth paying for again. */
    static final int PROMPT_VERSION = 1;

    static final int SUMMARY_LIMIT = 1200;

    private final SenseComparisonStore store;
    private final OpenAiChatClient model;
    private final SenseComparisonProperties properties;
    private final ObjectMapper objectMapper;
    private final Clock clock;

    record Draft(String summary) {}

    public SenseComparisonResponse compare(User user, SenseComparisonRequest request) {
        if (request.firstSenseKey().equals(request.secondSenseKey())) {
            throw new BadUserRequestActionException("A sense cannot be compared with itself.");
        }
        // One order for a pair, so "big vs large" and "large vs big" are one purchase.
        boolean inOrder = request.firstSenseKey().compareTo(request.secondSenseKey()) < 0;
        SenseFacts first = facts(inOrder ? request.firstSenseKey() : request.secondSenseKey());
        SenseFacts second = facts(inOrder ? request.secondSenseKey() : request.firstSenseKey());

        String system = systemPrompt(request.explanationLanguage());
        String facts = factsJson(first, second);
        String hash = sha256(PROMPT_VERSION + "\n" + system + "\n" + facts);

        Optional<String> bought = store.summary(hash);
        if (bought.isPresent() || !request.generate()) {
            return new SenseComparisonResponse(bought.orElse(null), false);
        }

        Instant now = clock.instant();
        if (store.boughtSince(user.getId(), now.minus(Duration.ofDays(1))) >= properties.getDailyCeiling()) {
            log.info("The daily ceiling on sense comparisons was reached for user {}", user.getId());
            return new SenseComparisonResponse(null, true);
        }

        OpenAiChatClient.Completion completion =
                model.complete(system, List.of(new OpenAiChatClient.Turn("user", facts)));
        String summary = parse(completion.content());
        store.insert(
                new SenseComparisonStore.NewComparison(
                        hash,
                        first.senseKey(),
                        second.senseKey(),
                        request.explanationLanguage().name().toLowerCase(Locale.ROOT),
                        summary,
                        completion.model(),
                        completion.promptTokens(),
                        completion.completionTokens(),
                        user.getId()),
                now);
        return new SenseComparisonResponse(summary, false);
    }

    private SenseFacts facts(UUID senseKey) {
        return store.facts(senseKey).orElseThrow(() -> new EntityNotFoundException("No such dictionary sense."));
    }

    static String systemPrompt(Language explanationLanguage) {
        String language = Locale.forLanguageTag(explanationLanguage.name().toLowerCase(Locale.ROOT))
                .getDisplayLanguage(Locale.ENGLISH);
        return """
                You help a language learner tell two words apart. You are given dictionary facts for one meaning of \
                each word: its definition, level, usage labels, grammar, sentence patterns, collocations, opposites \
                and examples. In at most three short sentences, say how the two differ in use and when to choose \
                each. Use only the facts given: do not add meanings, grammar, registers or collocations the facts \
                do not contain, and do not repeat the definitions back. If the facts show the two are unrelated, \
                or show no real difference, say that in one sentence. Write in %s, in plain words for a learner, \
                and keep the two words themselves and any quoted patterns in their own language. \
                Answer as a JSON object: {"summary": "..."}.""".formatted(language.isBlank() ? "English" : language);
    }

    private String factsJson(SenseFacts first, SenseFacts second) {
        try {
            return objectMapper.writeValueAsString(List.of(fields(first), fields(second)));
        } catch (JacksonException e) {
            throw new IllegalStateException("The facts of a sense could not be written", e);
        }
    }

    /** The facts in a fixed order with the empty ones left out, so equal content is equal text. */
    private static Map<String, Object> fields(SenseFacts facts) {
        Map<String, Object> fields = new LinkedHashMap<>();
        put(fields, "word", facts.headword());
        put(fields, "language", facts.language());
        put(fields, "part_of_speech", facts.partOfSpeech());
        put(fields, "definition", facts.definition());
        put(fields, "cefr", facts.cefr());
        put(fields, "labels", facts.labels());
        put(fields, "regions", facts.regions());
        put(fields, "word_grammar", facts.wordGrammar());
        put(fields, "sense_grammar", facts.grammar());
        put(fields, "patterns", facts.frames());
        put(fields, "collocations", facts.collocations());
        put(fields, "opposites", facts.antonyms());
        put(fields, "standard_word", facts.standardAlternative());
        if (!facts.examples().isEmpty()) {
            fields.put("examples", facts.examples());
        }
        return fields;
    }

    private static void put(Map<String, Object> fields, String name, String value) {
        if (value != null && !value.isBlank()) {
            fields.put(name, value);
        }
    }

    private String parse(String content) {
        try {
            Draft draft = objectMapper.readValue(content, Draft.class);
            String summary = draft == null || draft.summary() == null
                    ? ""
                    : draft.summary().trim();
            if (summary.isEmpty()) {
                throw new ApiIntegrationException("The model answered with an empty explanation");
            }
            return summary.length() > SUMMARY_LIMIT ? summary.substring(0, SUMMARY_LIMIT) : summary;
        } catch (JacksonException e) {
            throw new ApiIntegrationException("The model answered with something other than an explanation", e);
        }
    }

    private static String sha256(String text) {
        try {
            return HexFormat.of()
                    .formatHex(MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}

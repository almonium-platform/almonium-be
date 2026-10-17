package com.almonium.learning.lexicon.flow;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.config.PostgresContainer;
import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.lexicon.controller.internal.LexiconPublicationController;
import com.almonium.learning.lexicon.controller.open.LexiconOpenController;
import com.almonium.learning.lexicon.dto.response.EntryPublicationResult;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.LexemeView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.SenseView;
import com.almonium.learning.lexicon.mapper.LexiconEntryMapper;
import com.almonium.learning.lexicon.service.EntrySchema;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import com.almonium.learning.lexicon.service.LexiconPublicationService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * Two real entries, as the writer produced them, go in through the signed endpoint and come back out through the
 * public one; then the same entries again, which must change nothing but the version and leave pronunciations
 * where they stand, since audio clips will hang off them.
 */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Import({
    LexiconPublicationService.class,
    LexiconLookupService.class,
    LexiconEntryMapper.class,
    EntrySchema.class,
    LexiconPublicationFlowTest.Beans.class
})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class LexiconPublicationFlowTest {
    private static final String SECRET = "test-publisher-secret";
    private static final String WRITER = "cc-claude-opus-5-5";
    private static final Instant BUILT_AT = Instant.parse("2026-10-12T10:00:00Z");

    @TestConfiguration
    static class Beans {
        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        BooksSignature booksSignature() {
            return new BooksSignature(SECRET);
        }

        @Bean
        LexiconPublicationController publicationController(
                LexiconPublicationService service,
                EntrySchema schema,
                BooksSignature signature,
                ObjectMapper objectMapper) {
            return new LexiconPublicationController(service, schema, signature, objectMapper);
        }

        @Bean
        LexiconOpenController openController(LexiconLookupService lookupService) {
            return new LexiconOpenController(lookupService);
        }
    }

    @Autowired
    LexiconPublicationController publishing;

    @Autowired
    LexiconOpenController reading;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    void emptyLexicon() {
        jdbc.update("delete from lexicon.lexeme");
        jdbc.update("delete from lexicon.rejected_headword");
    }

    @Test
    void publishesTwoRealEntriesAndReadsThemBackByHeadwordLemmaAndForm() {
        List<EntryPublicationResult> results = publishBatch(publication("bloke"), publication("chuckle"));
        assertThat(results).extracting(EntryPublicationResult::accepted).containsOnly(true);
        assertThat(results).extracting(EntryPublicationResult::lexemes).containsExactly(1, 2);

        LexiconEntryResponse bloke = lookup("en", "bloke");
        assertThat(bloke.lexemes()).hasSize(1);
        LexemeView noun = bloke.lexemes().getFirst();
        assertThat(noun.matchedForm()).isNull();
        assertThat(noun.pos()).isEqualTo("NOUN");
        assertThat(noun.homographNo()).isEqualTo(1);
        assertThat(noun.grammar()).containsEntry("countability", "Count");
        assertThat(noun.forms()).extracting(form -> form.form()).containsExactly("bloke", "blokes");
        assertThat(noun.forms().get(1).features()).containsEntry("Number", "Plur");
        assertThat(noun.pronunciations()).hasSize(4);
        assertThat(noun.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation -> pronunciation.variety() + " " + pronunciation.phonemic()))
                .containsExactlyInAnyOrder("en-US /ˈbloʊk/", "en-GB /ˈbləʊk/");
        assertThat(noun.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() != null)
                        .map(pronunciation -> pronunciation.phonemic()))
                .containsExactlyInAnyOrder("/ˈbloʊks/", "/ˈbləʊks/");
        assertThat(noun.pronunciations())
                .allSatisfy(pronunciation -> assertThat(pronunciation.status()).isEqualTo("provisional"));
        SenseView sense = noun.senses().getFirst();
        assertThat(sense.register()).isEqualTo("informal");
        assertThat(sense.cefr()).isEqualTo("B2");
        assertThat(sense.cefrSource()).isEqualTo("writer");
        assertThat(sense.usage()).isEqualTo("British English");
        assertThat(sense.glosses().get("uk")).extracting(gloss -> gloss.text()).containsExactly("мужик");
        assertThat(sense.glosses().get("uk").getFirst().exact()).isNull();
        assertThat(sense.examples()).extracting(example -> example.origin()).containsExactly("generated");
        assertThat(sense.cues()).extracting(cue -> cue.kind()).containsExactly("wording", "wording", "cloze");
        assertThat(noun.relations()).hasSize(1);
        assertThat(noun.relations().getFirst().lemma()).isEqualTo("blokey");
        assertThat(noun.relations().getFirst().lexemeId()).isNull();
        assertThat(noun.capabilities().forms()).isTrue();
        assertThat(noun.capabilities().senseCefr()).isTrue();
        assertThat(noun.capabilities().ipa()).isTrue();
        assertThat(noun.capabilities().ipaAudio()).isFalse();
        assertThat(noun.capabilities().parts()).isFalse();
        assertThat(noun.provenance().writer()).isEqualTo(WRITER);
        assertThat(noun.provenance().model()).isEqualTo("claude-opus-5-5");
        assertThat(noun.provenance().promptVersion()).isEqualTo("v1");
        assertThat(noun.provenance().registerVersion()).isEqualTo("v3");
        assertThat(noun.provenance().schemaVersion()).isEqualTo("2");
        assertThat(noun.provenance().entryVersion()).isEqualTo(1);
        assertThat(noun.provenance().builtAt()).isEqualTo(BUILT_AT);

        LexiconEntryResponse chuckle = lookup("en", "Chuckle");
        assertThat(chuckle.lexemes()).extracting(LexemeView::pos).containsExactly("NOUN", "VERB");
        LexemeView verb = chuckle.lexemes().get(1);
        LexemeView chuckleNoun = chuckle.lexemes().get(0);
        assertThat(verb.forms()).hasSize(5);
        assertThat(verb.forms().get(1).features()).containsEntry("Person", 3);
        assertThat(verb.pronunciations()).hasSize(10);
        assertThat(verb.senses().getFirst().grammar()).containsEntry("valency", "Intr");
        assertThat(verb.senses().getFirst().glosses().get("de").getFirst().text())
                .isEqualTo("leise lachen");
        assertThat(verb.senses().getFirst().glosses().get("de").getFirst().exact())
                .isFalse();
        assertThat(verb.relations().getFirst().lexemeId()).isEqualTo(chuckleNoun.id());
        assertThat(chuckleNoun.relations().getFirst().lexemeId()).isEqualTo(verb.id());

        LexiconEntryResponse chuckled = lookup("en", "chuckled");
        assertThat(chuckled.lexemes()).hasSize(1);
        assertThat(chuckled.lexemes().getFirst().pos()).isEqualTo("VERB");
        assertThat(chuckled.lexemes().getFirst().matchedForm().form()).isEqualTo("chuckled");
        assertThat(chuckled.lexemes().getFirst().matchedForm().features()).containsEntry("Tense", "Past");

        LexiconEntryResponse blokes = lookup("en", "BLOKES");
        assertThat(blokes.lexemes().getFirst().matchedForm().form()).isEqualTo("blokes");

        assertThat(reading.entries("en", "nonesuch").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(reading.entries("de", "bloke").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void publishingTheSameEntryAgainChangesOnlyTheVersionAndKeepsPronunciationsWhereTheyStand() {
        publishOne(publication("chuckle"));
        Map<String, Integer> before = counts();
        Set<UUID> pronunciations = pronunciationIds();
        jdbc.update("update lexicon.pronunciation set status = 'verified'");

        publishOne(publication("chuckle"));

        Map<String, Integer> after = counts();
        assertThat(after)
                .containsAllEntriesOf(Map.of(
                        "lexeme", before.get("lexeme"),
                        "lexeme_form", before.get("lexeme_form"),
                        "pronunciation", before.get("pronunciation"),
                        "sense", before.get("sense"),
                        "sense_gloss", before.get("sense_gloss"),
                        "sense_example", before.get("sense_example"),
                        "sense_cue", before.get("sense_cue"),
                        "lexeme_relation", before.get("lexeme_relation")));
        assertThat(after.get("lexicon_build")).isEqualTo(before.get("lexicon_build") + 2);
        assertThat(pronunciationIds()).isEqualTo(pronunciations);
        assertThat(jdbc.queryForObject(
                        "select count(*) from lexicon.pronunciation where status = 'verified'", Integer.class))
                .isEqualTo(before.get("pronunciation"));
        assertThat(jdbc.queryForObject(
                        "select count(*) from lexicon.pronunciation p join lexicon.lexeme_form f on f.id = p.lexeme_form_id",
                        Integer.class))
                .isEqualTo(10);
        LexiconEntryResponse chuckle = lookup("en", "chuckle");
        assertThat(chuckle.lexemes())
                .allSatisfy(
                        lexeme -> assertThat(lexeme.provenance().entryVersion()).isEqualTo(2));
        assertThat(chuckle.lexemes().get(1).relations().getFirst().lexemeId())
                .isEqualTo(chuckle.lexemes().get(0).id());
    }

    @Test
    void aLexemeTheEntryNoLongerListsIsDeletedAndAChangedTranscriptionIsProvisionalAgain() {
        publishOne(publication("chuckle"));
        jdbc.update("update lexicon.pronunciation set status = 'verified'");
        ObjectNode entry = entry("chuckle");
        ((ArrayNode) entry.get("lexemes")).remove(1);
        ((ObjectNode) entry.get("lexemes").get(0).get("pronunciations").get(0)).put("ipa", "/ˈtʃʌkl/");
        // The writer dropped the transcription of "chuckles", whose rows must go while the lexeme's own stay.
        ((ObjectNode) entry.get("lexemes").get(0).get("forms").get(1)).remove("ipa");

        publishOne(publication(entry, meta("chuckle")));

        LexiconEntryResponse chuckle = lookup("en", "chuckle");
        assertThat(chuckle.lexemes()).extracting(LexemeView::pos).containsExactly("VERB");
        assertThat(chuckle.lexemes().getFirst().relations().getFirst().lexemeId())
                .isNull();
        assertThat(chuckle.lexemes().getFirst().pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation -> pronunciation.variety() + " " + pronunciation.status()))
                .containsExactlyInAnyOrder("en-US provisional", "en-GB verified");
        assertThat(chuckle.lexemes().getFirst().pronunciations()).hasSize(8);
        assertThat(counts().get("lexeme")).isEqualTo(1);
        assertThat(counts().get("pronunciation")).isEqualTo(8);
    }

    @Test
    void aRejectedHeadwordIsRecordedUntilAnEntryWithLexemesArrives() {
        publishOne(publication("bloke"));
        ObjectNode rejection = objectMapper.createObjectNode();
        rejection.put("language", "en");
        rejection.put("headword", "blokes");
        rejection.putArray("lexemes");
        rejection.put("reject", "inflected form of bloke");

        EntryPublicationResult result = publishOne(publication(rejection, meta("bloke")));

        assertThat(result.accepted()).isTrue();
        assertThat(result.lexemes()).isZero();
        assertThat(jdbc.queryForObject(
                        "select reason from lexicon.rejected_headword where language = 'en' and headword = 'blokes'",
                        String.class))
                .isEqualTo("inflected form of bloke");
        assertThat(lookup("en", "blokes").lexemes().getFirst().lemma()).isEqualTo("bloke");

        ObjectNode entry = entry("bloke");
        entry.put("headword", "blokes");
        publishOne(publication(entry, meta("bloke")));
        assertThat(jdbc.queryForObject("select count(*) from lexicon.rejected_headword", Integer.class))
                .isZero();
        assertThat(lookup("en", "bloke").lexemes().getFirst().headword()).isEqualTo("blokes");
    }

    private LexiconEntryResponse lookup(String language, String text) {
        ResponseEntity<LexiconEntryResponse> response = reading.entries(language, text);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
    }

    private EntryPublicationResult publishOne(ObjectNode publication) {
        byte[] body = objectMapper.writeValueAsBytes(publication);
        long timestamp = System.currentTimeMillis() / 1000;
        ResponseEntity<?> response = publishing.publish(timestamp, sign(timestamp, body), body);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return (EntryPublicationResult) response.getBody();
    }

    @SuppressWarnings("unchecked")
    private List<EntryPublicationResult> publishBatch(ObjectNode... publications) {
        ArrayNode batch = objectMapper.createArrayNode();
        for (ObjectNode publication : publications) {
            batch.add(publication);
        }
        byte[] body = objectMapper.writeValueAsBytes(batch);
        long timestamp = System.currentTimeMillis() / 1000;
        ResponseEntity<?> response = publishing.publish(timestamp, sign(timestamp, body), body);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return (List<EntryPublicationResult>) response.getBody();
    }

    private ObjectNode publication(String headword) {
        return publication(entry(headword), meta(headword));
    }

    private ObjectNode publication(JsonNode entry, JsonNode meta) {
        ObjectNode publication = objectMapper.createObjectNode();
        publication.put("writer", WRITER);
        publication.set("entry", entry);
        publication.set("meta", meta);
        publication.put("built_at", BUILT_AT.toString());
        return publication;
    }

    private ObjectNode entry(String headword) {
        return (ObjectNode) read("lexicon/" + headword + ".json");
    }

    private JsonNode meta(String headword) {
        return read("lexicon/" + headword + ".meta.json");
    }

    private JsonNode read(String resource) {
        try {
            return objectMapper.readTree(new ClassPathResource(resource).getContentAsByteArray());
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private Map<String, Integer> counts() {
        return List.of(
                        "lexeme",
                        "lexeme_form",
                        "pronunciation",
                        "sense",
                        "sense_gloss",
                        "sense_example",
                        "sense_cue",
                        "lexeme_relation",
                        "lexicon_build")
                .stream()
                .collect(Collectors.toMap(
                        table -> table,
                        table -> jdbc.queryForObject("select count(*) from lexicon." + table, Integer.class)));
    }

    private Set<UUID> pronunciationIds() {
        return Set.copyOf(jdbc.queryForList("select id from lexicon.pronunciation", UUID.class));
    }

    private static String sign(long timestamp, byte[] body) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((timestamp + ".").getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(mac.doFinal(body));
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }
    }
}

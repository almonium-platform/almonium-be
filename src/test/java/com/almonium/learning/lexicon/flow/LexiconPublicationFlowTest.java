package com.almonium.learning.lexicon.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

import com.almonium.analyzer.analyzer.controller.open.DiscoverController;
import com.almonium.analyzer.analyzer.dto.DiscoverLookupResponse;
import com.almonium.analyzer.analyzer.mapper.DiscoverLexiconMapper;
import com.almonium.analyzer.analyzer.service.DiscoverLookupService;
import com.almonium.analyzer.analyzer.service.FrequencyService;
import com.almonium.analyzer.analyzer.service.LanguageProcessor;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import com.almonium.learning.book.controller.internal.BooksSignature;
import com.almonium.learning.lexicon.audio.AudioStore;
import com.almonium.learning.lexicon.audio.FileSystemAudioStore;
import com.almonium.learning.lexicon.controller.internal.LexiconPublicationController;
import com.almonium.learning.lexicon.controller.internal.PronunciationClipController;
import com.almonium.learning.lexicon.controller.open.LexiconClipController;
import com.almonium.learning.lexicon.controller.open.LexiconOpenController;
import com.almonium.learning.lexicon.dto.response.ClipSubmissionResult;
import com.almonium.learning.lexicon.dto.response.EntryPublicationResult;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ClipView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.LexemeView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.PronunciationView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.SenseView;
import com.almonium.learning.lexicon.dto.response.UnvoicedPronunciation;
import com.almonium.learning.lexicon.mapper.AudioClipMapper;
import com.almonium.learning.lexicon.mapper.LexiconEntryMapper;
import com.almonium.learning.lexicon.model.ClipObjectKey;
import com.almonium.learning.lexicon.service.AudioClipService;
import com.almonium.learning.lexicon.service.EntrySchema;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import com.almonium.learning.lexicon.service.LexiconPublicationService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Base64;
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
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

/**
 * Two real entries, as the writer produced them, go in through the signed endpoint and come back out through the
 * public one; then the same entries again, which must change nothing but the version and leave pronunciations
 * where they stand, since audio clips hang off them. Then a clip: listed as wanted, put once, read back with the
 * entry and by itself, and put again without a second write. Last, the Discover sheet, which reads this lexicon
 * before it asks any translation provider.
 */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Import({
    LexiconPublicationService.class,
    LexiconLookupService.class,
    LexiconEntryMapper.class,
    AudioClipService.class,
    AudioClipMapper.class,
    EntrySchema.class,
    DiscoverLexiconMapper.class,
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

        @Bean
        AudioStore audioStore() throws IOException {
            return new FileSystemAudioStore(Files.createTempDirectory("almonium-audio"));
        }

        @Bean
        PronunciationClipController clipController(
                AudioClipService service, BooksSignature signature, ObjectMapper objectMapper) {
            return new PronunciationClipController(service, signature, objectMapper);
        }

        @Bean
        LexiconClipController clipReading(AudioClipService service) {
            return new LexiconClipController(service);
        }

        @Bean
        LanguageProcessor languageProcessor() {
            return mock(LanguageProcessor.class);
        }

        @Bean
        FrequencyService frequencyService() {
            return mock(FrequencyService.class);
        }

        @Bean
        DiscoverController discoverController(
                FrequencyService frequencyService,
                LanguageProcessor languageProcessor,
                LexiconLookupService lookupService,
                DiscoverLexiconMapper mapper) {
            return new DiscoverController(
                    frequencyService,
                    new DiscoverLookupService(languageProcessor, frequencyService, lookupService, mapper));
        }
    }

    @Autowired
    LexiconPublicationController publishing;

    @Autowired
    LexiconOpenController reading;

    @Autowired
    PronunciationClipController voicing;

    @Autowired
    LexiconClipController listening;

    @Autowired
    DiscoverController discovering;

    @Autowired
    LanguageProcessor languageProcessor;

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

    @Test
    void aVarietyThatSaysAWordTwoWaysKeepsBothRankedAndInPlace() {
        ObjectNode entry = entry("bloke");
        ArrayNode spoken = (ArrayNode) entry.get("lexemes").get(0).get("pronunciations");
        spoken.insert(1, objectMapper.createObjectNode().put("variety", "en-US").put("ipa", "/ˈblɔʊk/"));
        publishOne(publication(entry, meta("bloke")));
        Set<UUID> pronunciations = pronunciationIds();

        publishOne(publication(entry, meta("bloke")));

        LexemeView bloke = lookup("en", "bloke").lexemes().getFirst();
        assertThat(bloke.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation ->
                                pronunciation.variety() + "#" + pronunciation.rank() + " " + pronunciation.phonemic()))
                .containsExactly("en-GB#1 /ˈbləʊk/", "en-US#1 /ˈbloʊk/", "en-US#2 /ˈblɔʊk/");
        assertThat(pronunciationIds()).isEqualTo(pronunciations);
    }

    @Test
    void aClipIsStoredOnceServedForeverAndShowsUpOnItsPronunciation() {
        publishOne(publication("bloke"));
        List<UnvoicedPronunciation> unvoiced = withoutClips("en");
        assertThat(unvoiced).hasSize(4);
        assertThat(unvoiced).allSatisfy(pronunciation -> {
            assertThat(pronunciation.headword()).isEqualTo("bloke");
            assertThat(pronunciation.lemma()).isEqualTo("bloke");
            assertThat(pronunciation.pos()).isEqualTo("NOUN");
        });
        assertThat(unvoiced.stream().filter(pronunciation -> pronunciation.formId() != null))
                .extracting(UnvoicedPronunciation::form)
                .containsOnly("blokes");
        UnvoicedPronunciation british = unvoiced.stream()
                .filter(pronunciation -> pronunciation.formId() == null
                        && pronunciation.variety().equals("en-GB"))
                .findFirst()
                .orElseThrow();
        assertThat(british.phonemic()).isEqualTo("/ˈbləʊk/");
        assertThat(british.engineInput()).isNull();
        byte[] mp3 = {(byte) 0xFF, (byte) 0xFB, (byte) 0x90, 0x64, 1, 2, 3};

        // An unverified take first: it is kept, with what was heard, but gives the lexeme no audio capability.
        ClipSubmissionResult doubtful = putClip(british.id(), clip(mp3, "Puck", "unverified", "broke"));
        assertThat(doubtful.stored()).isTrue();
        assertThat(lookup("en", "bloke").lexemes().getFirst().capabilities().ipaAudio())
                .isFalse();

        ClipSubmissionResult first = putClip(british.id(), clip(mp3, "Charon", "verified", null));
        assertThat(first.stored()).isTrue();
        assertThat(first.objectKey()).startsWith("audio/en-GB/Charon/").endsWith(".mp3");
        assertThat(first.clipId()).isNotEqualTo(doubtful.clipId());

        LexemeView bloke = lookup("en", "bloke").lexemes().getFirst();
        PronunciationView spoken = bloke.pronunciations().stream()
                .filter(pronunciation -> pronunciation.id().equals(british.id()))
                .findFirst()
                .orElseThrow();
        assertThat(spoken.clips()).extracting(ClipView::status).containsExactly("verified", "unverified");
        ClipView clip = spoken.clips().getFirst();
        assertThat(clip.id()).isEqualTo(first.clipId());
        assertThat(clip.engine()).isEqualTo("google");
        assertThat(clip.voice()).isEqualTo("Charon");
        assertThat(clip.pace()).isEqualByComparingTo("1.00");
        assertThat(clip.format()).isEqualTo("mp3_44100_128");
        assertThat(clip.durationMs()).isEqualTo(640);
        assertThat(clip.url()).isEqualTo("/public/lexicon/clips/" + first.clipId());
        assertThat(bloke.capabilities().ipaAudio()).isTrue();
        assertThat(bloke.pronunciations().stream()
                        .filter(pronunciation -> !pronunciation.id().equals(british.id())))
                .allSatisfy(pronunciation -> assertThat(pronunciation.clips()).isEmpty());
        assertThat(jdbc.queryForObject(
                        "select engine_input from lexicon.pronunciation where id = ?", String.class, british.id()))
                .isEqualTo("bloke");
        assertThat(jdbc.queryForObject(
                        "select transcript from lexicon.audio_clip where id = ?", String.class, doubtful.clipId()))
                .isEqualTo("broke");

        ResponseEntity<byte[]> served = listening.clip(first.clipId());
        assertThat(served.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(served.getBody()).isEqualTo(mp3);
        assertThat(served.getHeaders().getContentType()).hasToString("audio/mpeg");
        assertThat(served.getHeaders().getContentLength()).isEqualTo(mp3.length);
        assertThat(served.getHeaders().getCacheControl()).isEqualTo("public, max-age=31536000, immutable");
        assertThat(served.getHeaders().getETag()).isEqualTo("\"" + ClipObjectKey.hashOf(first.objectKey()) + "\"");
        assertThat(listening.clip(UUID.randomUUID()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        // The same rendering again: nothing is written, and the row keeps its id.
        ClipSubmissionResult again = putClip(british.id(), clip(mp3, "Charon", "verified", null));
        assertThat(again.stored()).isFalse();
        assertThat(again.clipId()).isEqualTo(first.clipId());
        assertThat(again.objectKey()).isEqualTo(first.objectKey());
        assertThat(jdbc.queryForObject("select count(*) from lexicon.audio_clip", Integer.class))
                .isEqualTo(2);
        assertThat(withoutClips("en"))
                .hasSize(3)
                .noneMatch(pronunciation -> pronunciation.id().equals(british.id()));

        ObjectNode wav = clip(mp3, "Charon", "verified", null).put("format", "wav_44100");
        assertThatThrownBy(() -> putClip(british.id(), wav))
                .isInstanceOfSatisfying(
                        ResponseStatusException.class,
                        exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));
        ObjectNode orphan = clip(mp3, "Charon", "verified", null);
        assertThatThrownBy(() -> putClip(UUID.randomUUID(), orphan))
                .isInstanceOfSatisfying(
                        ResponseStatusException.class,
                        exception -> assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void discoverServesThePublishedEntryWithItsClipAndNeverAsksTheProvider() {
        publishOne(publication("bloke"));
        UnvoicedPronunciation british = withoutClips("en").stream()
                .filter(pronunciation -> pronunciation.formId() == null
                        && pronunciation.variety().equals("en-GB"))
                .findFirst()
                .orElseThrow();
        byte[] mp3 = {(byte) 0xFF, (byte) 0xFB, (byte) 0x90, 0x64, 1, 2, 3};
        putClip(british.id(), clip(mp3, "Puck", "unverified", "broke"));
        ClipSubmissionResult verified = putClip(british.id(), clip(mp3, "Charon", "verified", null));

        ResponseEntity<DiscoverLookupResponse> response =
                discovering.lookup(Language.EN, Language.UK, "Bloke", "A nice bloke.");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DiscoverLookupResponse sheet = response.getBody();
        assertThat(sheet.provider()).isEqualTo("lexicon");
        assertThat(sheet.entry()).isEqualTo("Bloke");
        assertThat(sheet.sourceContext()).isEqualTo("A nice bloke.");
        assertThat(sheet.pronunciations())
                .extracting(pronunciation -> pronunciation.variety() + " " + pronunciation.ipa())
                .containsExactly("en-GB /ˈbləʊk/", "en-US /ˈbloʊk/");
        assertThat(sheet.pronunciations().getFirst().clipUrl()).isEqualTo("/public/lexicon/clips/" + verified.clipId());
        assertThat(sheet.pronunciations().getFirst().clipStatus()).isEqualTo("verified");
        assertThat(sheet.pronunciations().get(1).clipUrl()).isNull();
        assertThat(sheet.senses()).singleElement().satisfies(sense -> {
            assertThat(sense.index()).isEqualTo(1);
            assertThat(sense.headword()).isEqualTo("bloke");
            assertThat(sense.partOfSpeech()).isEqualTo("NOUN");
            assertThat(sense.transcription()).isEqualTo("/ˈbləʊk/");
            assertThat(sense.translations()).containsExactly("мужик");
            assertThat(sense.definition()).startsWith("a man, especially an ordinary one");
            assertThat(sense.cefr()).isEqualTo("B2");
            assertThat(sense.examples()).singleElement().satisfies(example -> {
                assertThat(example.text()).isEqualTo("He seems like a nice bloke, always happy to help.");
                assertThat(example.translation()).isNull();
            });
        });
        verifyNoInteractions(languageProcessor);
    }

    private List<UnvoicedPronunciation> withoutClips(String language) {
        long timestamp = System.currentTimeMillis() / 1000;
        return voicing.list(timestamp, sign(timestamp, new byte[0]), language, "clip", 500, 0);
    }

    private ClipSubmissionResult putClip(UUID pronunciationId, ObjectNode submission) {
        byte[] body = objectMapper.writeValueAsBytes(submission);
        long timestamp = System.currentTimeMillis() / 1000;
        return voicing.put(timestamp, sign(timestamp, body), pronunciationId, body);
    }

    private ObjectNode clip(byte[] mp3, String voice, String status, String transcript) {
        ObjectNode submission = objectMapper.createObjectNode();
        submission.put("engine", "google");
        submission.put("voice", voice);
        submission.put("model", "chirp3-hd");
        submission.put("pace", 1.0);
        submission.put("format", "mp3_44100_128");
        submission.put("engine_input", "bloke");
        submission.put("status", status);
        submission.put("duration_ms", 640);
        submission.put("transcript", transcript);
        submission.put("character_cost", 5.0);
        submission.put("audio_base64", Base64.getEncoder().encodeToString(mp3));
        return submission;
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

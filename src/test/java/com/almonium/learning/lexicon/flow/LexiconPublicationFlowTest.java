package com.almonium.learning.lexicon.flow;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.analyzer.controller.open.DiscoverController;
import com.almonium.analyzer.analyzer.dto.DiscoverLookupResponse;
import com.almonium.analyzer.analyzer.mapper.DiscoverLexiconMapper;
import com.almonium.analyzer.analyzer.service.DiscoverLookupRecorder;
import com.almonium.analyzer.analyzer.service.DiscoverLookupService;
import com.almonium.analyzer.analyzer.service.DiscoverMemberLanguages;
import com.almonium.analyzer.analyzer.service.DiscoverMemberLanguages.MemberLanguages;
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
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.FormView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.LexemeView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.PronunciationView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.SenseView;
import com.almonium.learning.lexicon.dto.response.LexiconSuggestion;
import com.almonium.learning.lexicon.dto.response.SenseHistoryResponse;
import com.almonium.learning.lexicon.dto.response.UnvoicedPronunciation;
import com.almonium.learning.lexicon.mapper.AudioClipMapper;
import com.almonium.learning.lexicon.mapper.LexiconEntryMapper;
import com.almonium.learning.lexicon.model.ClipObjectKey;
import com.almonium.learning.lexicon.model.RegionScope;
import com.almonium.learning.lexicon.service.AudioClipService;
import com.almonium.learning.lexicon.service.EntrySchema;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import com.almonium.learning.lexicon.service.LexiconPublicationService;
import com.almonium.learning.lexicon.service.SenseHistoryService;
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
 * Two real entries, as the ingest step publishes what the writer produced, go in through the signed endpoint and come back out through the
 * public one; then the same entries again, which must change nothing but the version and leave pronunciations
 * where they stand, since audio clips hang off them. Then a clip: listed as wanted, put once, read back with the
 * entry and by itself, and put again without a second write. Last, the Discover sheet, which reads this lexicon
 * before it asks any translation provider.
 */
@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
@Import({
    LexiconPublicationService.class,
    SenseHistoryService.class,
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
    private static final String WRITER = "cc-claude-opus-5-5-v4";
    private static final UUID RADIO_SENSE = UUID.fromString("6f900ca0-89af-50ea-a5ad-043ca90b0b0a");
    private static final UUID INSECT_SENSE = UUID.fromString("5fab857a-efa5-50c7-9e0c-e07956b5dc27");
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
        DiscoverMemberLanguages discoverMemberLanguages() {
            DiscoverMemberLanguages memberLanguages = mock(DiscoverMemberLanguages.class);
            when(memberLanguages.current()).thenReturn(MemberLanguages.GUEST);
            return memberLanguages;
        }

        @Bean
        DiscoverController discoverController(
                FrequencyService frequencyService,
                LanguageProcessor languageProcessor,
                LexiconLookupService lookupService,
                DiscoverLexiconMapper mapper,
                DiscoverMemberLanguages memberLanguages) {
            return new DiscoverController(
                    frequencyService,
                    new DiscoverLookupService(
                            languageProcessor,
                            frequencyService,
                            lookupService,
                            mapper,
                            mock(DiscoverLookupRecorder.class),
                            memberLanguages));
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

    @Autowired
    SenseHistoryService senseHistory;

    @BeforeEach
    void emptyLexicon() {
        jdbc.update("delete from lexicon.lexeme");
        jdbc.update("delete from lexicon.rejected_headword");
        jdbc.update("delete from lexicon.entry_version");
        jdbc.update("delete from lexicon.entry_alias");
    }

    @Test
    void publishesTwoRealEntriesAndReadsThemBackByHeadwordLemmaAndForm() {
        List<EntryPublicationResult> results = publishBatch(publication("antenna"), publication("program"));
        assertThat(results).extracting(EntryPublicationResult::message).containsOnlyNulls();
        assertThat(results).extracting(EntryPublicationResult::accepted).containsOnly(true);
        assertThat(results).extracting(EntryPublicationResult::lexemes).containsExactly(1, 2);

        LexiconEntryResponse antenna = lookup("en", "antenna");
        assertThat(antenna.lexemes()).hasSize(1);
        LexemeView noun = antenna.lexemes().getFirst();
        assertThat(noun.matchedForm()).isNull();
        assertThat(noun.pos()).isEqualTo("NOUN");
        assertThat(noun.homographNo()).isEqualTo(1);
        assertThat(noun.kind()).isEqualTo("word");
        assertThat(noun.grammar()).containsEntry("countability", "Count");
        assertThat(noun.spellings()).isNull();
        assertThat(noun.forms()).extracting(FormView::form).containsExactly("antennae");
        assertThat(noun.forms().getFirst().features()).containsEntry("Number", "Plur");
        assertThat(noun.forms().getFirst().senseKeys()).containsExactly(INSECT_SENSE);
        assertThat(noun.pronunciations()).hasSize(4);
        assertThat(noun.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation -> pronunciation.variety() + " " + pronunciation.phonemic()))
                .containsExactlyInAnyOrder("en-US /æn.ˈtɛn.ə/", "en-GB /æn.ˈten.ə/");
        assertThat(noun.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() != null)
                        .map(PronunciationView::phonemic))
                .containsExactlyInAnyOrder("/æn.ˈtɛn.iː/", "/æn.ˈten.iː/");
        assertThat(noun.pronunciations())
                .allSatisfy(pronunciation -> assertThat(pronunciation.status()).isEqualTo("provisional"));
        assertThat(noun.senses()).extracting(SenseView::key).containsExactly(RADIO_SENSE, INSECT_SENSE);
        SenseView sense = noun.senses().getFirst();
        assertThat(sense.definition()).startsWith("a metal rod or wire");
        assertThat(sense.cefr()).isEqualTo("B2");
        assertThat(sense.cefrSource()).isEqualTo("writer");
        assertThat(sense.labels()).isNull();
        assertThat(sense.regions()).isNull();
        assertThat(sense.glosses()).isEmpty();
        assertThat(sense.cues()).isEmpty();
        assertThat(sense.examples()).singleElement().satisfies(example -> {
            assertThat(example.origin()).isEqualTo("generated");
            assertThat(example.text()).isEqualTo("We put a new antenna on the roof to improve reception.");
        });
        assertThat(noun.relations()).isEmpty();
        assertThat(noun.capabilities().forms()).isTrue();
        assertThat(noun.capabilities().senseCefr()).isTrue();
        assertThat(noun.capabilities().ipa()).isTrue();
        assertThat(noun.capabilities().ipaAudio()).isFalse();
        assertThat(noun.capabilities().parts()).isFalse();
        assertThat(noun.capabilities().relations()).isFalse();
        assertThat(noun.provenance().writer()).isEqualTo(WRITER);
        assertThat(noun.provenance().model()).isEqualTo("claude-opus-5-5");
        assertThat(noun.provenance().promptVersion()).isEqualTo("v4");
        assertThat(noun.provenance().schemaVersion()).isEqualTo("4");
        assertThat(noun.provenance().entryVersion()).isEqualTo(1);
        assertThat(noun.provenance().builtAt()).isEqualTo(BUILT_AT);

        LexiconEntryResponse program = lookup("en", "Program");
        assertThat(program.lexemes()).extracting(LexemeView::pos).containsExactly("NOUN", "VERB");
        LexemeView programNoun = program.lexemes().get(0);
        LexemeView verb = program.lexemes().get(1);
        assertThat(programNoun.spellings())
                .extracting(spelling -> spelling.get("text") + " " + spelling.get("varieties"))
                .containsExactly("program [en-US, en-AU]", "programme [en-GB, en-IN]");
        SenseView broadcast = programNoun.senses().getFirst();
        assertThat(broadcast.frames()).containsExactly("a program about sth");
        assertThat(broadcast.collocations()).containsExactly("a program on TV");
        assertThat(broadcast.patterns()).singleElement().satisfies(pattern -> {
            assertThat(pattern).containsEntry("frame", "a program about sth");
            assertThat(pattern.get("parts"))
                    .isEqualTo(List.of(
                            Map.of("word", "a"),
                            Map.of("head", "program"),
                            Map.of("word", "about"),
                            Map.of("slot", List.of("thing"))));
        });
        assertThat(programNoun.senses().get(2).labels()).containsEntry("domain", "computing");
        assertThat(verb.forms()).extracting(FormView::form).containsExactly("programmed", "programming");
        assertThat(verb.forms().getFirst().features()).containsEntry("Tense", "Past");
        assertThat(verb.forms().getFirst().senseKeys()).isNull();
        assertThat(verb.pronunciations()).hasSize(6);
        assertThat(verb.senses().getFirst().grammar()).containsEntry("valency", "Both");
        assertThat(verb.senses().get(2).labels()).containsEntry("figurative", true);
        assertThat(verb.relations()).singleElement().satisfies(relation -> {
            assertThat(relation.type()).isEqualTo("derived_from");
            assertThat(relation.lexemeId()).isEqualTo(programNoun.id());
            assertThat(relation.senses()).isNull();
        });

        LexiconEntryResponse programming = lookup("en", "programming");
        assertThat(programming.lexemes()).hasSize(1);
        assertThat(programming.lexemes().getFirst().pos()).isEqualTo("VERB");
        assertThat(programming.lexemes().getFirst().matchedForm().form()).isEqualTo("programming");
        assertThat(programming.lexemes().getFirst().matchedForm().features()).containsEntry("Tense", "Pres");

        LexiconEntryResponse antennae = lookup("en", "ANTENNAE");
        assertThat(antennae.lexemes().getFirst().matchedForm().form()).isEqualTo("antennae");

        assertThat(reading.entries("en", "nonesuch").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(reading.entries("de", "antenna").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void aSpellingTheEntryAnswersForFindsItAndGoesWhenTheEntryNoLongerListsIt() {
        ObjectNode entry = entry("program");
        ((ArrayNode) entry.get("aliases")).add("programs");
        publishOne(publication(entry, meta("program")));

        // Another spelling of the lexemes, and a form that was asked for as a headword and is not listed as a form.
        assertThat(lookup("en", "Programme").lexemes())
                .extracting(lexeme -> lexeme.headword() + " " + lexeme.pos())
                .containsExactly("program NOUN", "program VERB");
        assertThat(lookup("en", "programs").lexemes())
                .allSatisfy(lexeme -> assertThat(lexeme.matchedForm()).isNull());
        // A word that is a listed form is answered by that form's lexeme alone, though the entry lists it too.
        assertThat(lookup("en", "programmed").lexemes()).singleElement().satisfies(lexeme -> {
            assertThat(lexeme.pos()).isEqualTo("VERB");
            assertThat(lexeme.matchedForm().form()).isEqualTo("programmed");
        });
        assertThat(jdbc.queryForList(
                        "select alias from lexicon.entry_alias where headword = 'program' order by alias",
                        String.class))
                .containsExactly("programme", "programmed", "programs");

        publishOne(publication("program"));

        assertThat(reading.entries("en", "programs").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(lookup("en", "programme").lexemes()).hasSize(2);
    }

    @Test
    void aMeasuredFrequencyIsKeptOnEveryLexemeAndRanksTheSuggestions() {
        ObjectNode antenna = publication("antenna");
        antenna.put("zipf", 3.1);
        ObjectNode program = publication("program");
        program.put("zipf", 3.456);
        assertThat(publishBatch(antenna, program))
                .extracting(EntryPublicationResult::accepted)
                .containsOnly(true);

        assertThat(lookup("en", "antenna").lexemes())
                .extracting(LexemeView::zipf)
                .containsExactly(3.1);
        assertThat(lookup("en", "program").lexemes())
                .extracting(LexemeView::zipf)
                .containsOnly(3.46);

        // An entry carries no glosses, so a suggestion has none until the gloss pass has run.
        assertThat(suggestions("en", "Ant", "uk"))
                .containsExactly(new LexiconSuggestion("antenna", "NOUN", null, null, 3.1));
        assertThat(suggestions("en", "program", null))
                .extracting(LexiconSuggestion::headword)
                .containsExactly("program");
        assertThat(suggestions("en", "programmi", "uk"))
                .extracting(LexiconSuggestion::headword)
                .containsExactly("program");
        assertThat(suggestions("en", "%", "uk")).isEmpty();
        assertThat(suggestions("en", "_ntenna", "uk")).isEmpty();
    }

    @Test
    void keepsWhereAFormAndASenseAreUsedWhatKindOfWordASenseIsAndHowAVarietyReadsIt() {
        ObjectNode antenna = publication("antenna");
        ObjectNode lexeme = (ObjectNode) antenna.path("entry").path("lexemes").get(0);
        ((ObjectNode) lexeme.path("forms").get(0)).putArray("varieties").add("en-GB");
        ((ObjectNode) lexeme.path("forms").get(0)).put("note", "in the insect sense");
        ObjectNode sense = (ObjectNode) lexeme.path("senses").get(0);
        // A region need not be a variety the product transcribes: Irish English, or English across the Caribbean.
        // A bare variety means only there.
        ArrayNode regions = sense.putArray("regions");
        regions.add("en-IE");
        regions.addObject().put("variety", "en-029").put("extent", "mainly");
        ObjectNode labels = sense.putObject("labels");
        labels.put("formality", "informal").put("domain", "technology");
        labels.putArray("attitude").add("humorous");
        sense.put("standard_alternative", "aerial");
        sense.putArray("antonyms").addObject().put("lemma", "receiver").put("pos", "NOUN");
        ((ArrayNode) lexeme.path("pronunciations"))
                .addObject()
                .put("variety", "en-GB")
                .put("ipa", "/æn.ˈtɛn.ə/")
                .put("reading", "anténna")
                .put("notation", "stress")
                .put("note", "older");
        lexeme.putArray("relations")
                .addObject()
                .put("type", "derived_from")
                .put("lemma", "antenn")
                .put("pos", "NOUN")
                .putArray("senses")
                .add(2);

        assertThat(publishBatch(antenna))
                .extracting(EntryPublicationResult::message)
                .containsOnlyNulls();

        LexemeView noun = lookup("en", "antenna").lexemes().getFirst();
        assertThat(noun.forms().getFirst().varieties()).containsExactly("en-GB");
        assertThat(noun.forms().getFirst().note()).isEqualTo("in the insect sense");
        SenseView read = noun.senses().getFirst();
        assertThat(read.regions())
                .containsExactly(new RegionScope("en-IE", "only"), new RegionScope("en-029", "mainly"));
        assertThat(read.labels())
                .containsEntry("formality", "informal")
                .containsEntry("domain", "technology")
                .containsEntry("attitude", List.of("humorous"));
        assertThat(read.standardAlternative()).isEqualTo("aerial");
        assertThat(read.antonyms()).containsExactly(Map.of("lemma", "receiver", "pos", "NOUN"));
        assertThat(noun.relations().getFirst().senses()).containsExactly(2);
        assertThat(noun.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation -> pronunciation.variety() + " " + pronunciation.rank() + " "
                                + pronunciation.note() + " " + pronunciation.reading() + " "
                                + pronunciation.notation()))
                .containsExactlyInAnyOrder(
                        "en-US 1 null null null", "en-GB 1 null null null", "en-GB 2 older anténna stress");
    }

    @Test
    void aWordWithAReadingAndNoTranscriptionIsPublishedAndTheSheetShowsItsSenses() {
        ObjectNode entry = entry("antenna");
        entry.put("language", "ja");
        entry.put("headword", "箸");
        ObjectNode lexeme = (ObjectNode) entry.get("lexemes").get(0);
        lexeme.put("lemma", "箸");
        lexeme.remove("forms");
        lexeme.putArray("pronunciations")
                .addObject()
                .put("variety", "ja-JP")
                .put("reading", "はし")
                .put("notation", "kana")
                .put("accent", 1);

        assertThat(publishBatch(publication(entry, meta("antenna"))))
                .extracting(EntryPublicationResult::message)
                .containsOnlyNulls();

        LexemeView noun = lookup("ja", "箸").lexemes().getFirst();
        assertThat(noun.pronunciations()).singleElement().satisfies(pronunciation -> {
            assertThat(pronunciation.variety()).isEqualTo("ja-JP");
            assertThat(pronunciation.phonemic()).isNull();
            assertThat(pronunciation.reading()).isEqualTo("はし");
            assertThat(pronunciation.notation()).isEqualTo("kana");
            assertThat(pronunciation.accent()).isEqualTo(1);
        });
        assertThat(noun.capabilities().ipa()).isFalse();
        // The sheet has no place for a reading yet: the senses are there, without a transcription.
        DiscoverLookupResponse sheet =
                discovering.lookup(Language.JA, Language.UK, "箸", null).getBody();
        assertThat(sheet.pronunciations()).isEmpty();
        assertThat(sheet.senses())
                .hasSize(2)
                .allSatisfy(sense -> assertThat(sense.transcription()).isNull());
    }

    /** {@code from} was the evidence pipeline's note on a sense; ingest drops it and the published schema has no such key. */
    @Test
    void aSenseThatStillCarriesTheEvidencePipelinesFromIsRefused() {
        ObjectNode antenna = publication("antenna");
        ObjectNode sense = (ObjectNode)
                antenna.path("entry").path("lexemes").get(0).path("senses").get(0);
        sense.putArray("from").add("1.1").add("1.2");

        assertThat(publishBatch(antenna))
                .extracting(EntryPublicationResult::accepted)
                .containsOnly(false);
    }

    @Test
    void aVarietyTheCatalogueLacksForTheLanguageRefusesThePublication() {
        ObjectNode program = publication("program");
        ObjectNode lexeme = (ObjectNode) program.path("entry").path("lexemes").get(0);
        ((ObjectNode) lexeme.path("pronunciations").get(0)).put("variety", "en-NZ");
        ((ArrayNode) lexeme.path("spellings").get(1).path("varieties")).add("en-ZA");
        ObjectNode sense = (ObjectNode) lexeme.path("senses").get(0);
        sense.putArray("regions")
                .add("pt-BR")
                .addObject()
                .put("variety", "en-999")
                .put("extent", "only");

        assertThat(publishBatch(program)).singleElement().satisfies(result -> {
            assertThat(result.accepted()).isFalse();
            assertThat(result.message())
                    .contains("/lexemes/0/pronunciations/0: en-NZ is not a variety of en")
                    .contains("/lexemes/0/spellings/1/varieties/2: en-ZA is not a variety of en")
                    .contains("/lexemes/0/senses/0/regions/0: pt-BR is not en with a country or a UN region")
                    .contains("/lexemes/0/senses/0/regions/1: en-999 is not en with a country or a UN region");
        });
        assertThat(jdbc.queryForObject("select count(*) from lexicon.lexeme", Integer.class))
                .isZero();
    }

    @Test
    void anEntryAsAWriterReturnedItWithoutTheIdsIngestGivesIsRefused() {
        ObjectNode antenna = publication("antenna");
        ObjectNode lexeme = (ObjectNode) antenna.path("entry").path("lexemes").get(0);
        lexeme.remove("id");
        ((ObjectNode) lexeme.path("senses").get(0)).remove("id");

        assertThat(publishBatch(antenna)).singleElement().satisfies(result -> {
            assertThat(result.accepted()).isFalse();
            assertThat(result.message()).contains("id");
        });
    }

    @Test
    void aFrequencyOffTheZipfScaleRefusesThePublication() {
        ObjectNode antenna = publication("antenna");
        antenna.put("zipf", 12.0);
        assertThat(publishBatch(antenna)).singleElement().satisfies(result -> {
            assertThat(result.accepted()).isFalse();
            assertThat(result.message()).contains("zipf");
        });
    }

    @Test
    void publishingTheSameEntryAgainChangesOnlyTheVersionAndKeepsPronunciationsWhereTheyStand() {
        publishOne(publication("program"));
        Map<String, Integer> before = counts();
        Set<UUID> pronunciations = pronunciationIds();
        List<UUID> senseKeys = senseKeys(lookup("en", "program"));
        jdbc.update("update lexicon.pronunciation set status = 'verified'");

        publishOne(publication("program"));

        Map<String, Integer> after = counts();
        assertThat(after)
                .containsAllEntriesOf(Map.of(
                        "lexeme", before.get("lexeme"),
                        "lexeme_form", before.get("lexeme_form"),
                        "pronunciation", before.get("pronunciation"),
                        "sense", before.get("sense"),
                        "sense_example", before.get("sense_example"),
                        "lexeme_relation", before.get("lexeme_relation"),
                        "entry_alias", before.get("entry_alias")));
        assertThat(after.get("lexicon_build")).isEqualTo(before.get("lexicon_build") + 2);
        assertThat(pronunciationIds()).isEqualTo(pronunciations);
        assertThat(jdbc.queryForObject(
                        "select count(*) from lexicon.pronunciation where status = 'verified'", Integer.class))
                .isEqualTo(before.get("pronunciation"));
        assertThat(jdbc.queryForObject(
                        "select count(*) from lexicon.pronunciation p join lexicon.lexeme_form f on f.id = p.lexeme_form_id",
                        Integer.class))
                .isEqualTo(4);
        LexiconEntryResponse program = lookup("en", "program");
        assertThat(senseKeys(program)).isEqualTo(senseKeys);
        assertThat(program.lexemes())
                .allSatisfy(
                        lexeme -> assertThat(lexeme.provenance().entryVersion()).isEqualTo(2));
        assertThat(program.lexemes().get(1).relations().getFirst().lexemeId())
                .isEqualTo(program.lexemes().get(0).id());
    }

    @Test
    void aSenseIsKeyedByTheIdItsEntryGivesItAndItsHistoryShowsTheVersionThatDroppedIt() {
        publishOne(publication("program"));
        LexiconEntryResponse first = lookup("en", "program");
        UUID kept = first.lexemes().get(1).senses().get(0).key();
        UUID dropped = first.lexemes().get(1).senses().get(2).key();
        UUID ofNoun = first.lexemes().get(0).senses().get(0).key();
        assertThat(kept).isEqualTo(UUID.fromString("8d29cb55-410f-56dc-b5a1-60811045a1f4"));

        // As ingest writes a rewrite: the sense whose definition changed a little keeps its id, the one that is
        // gone is listed as retired, and the lexeme that is gone takes its senses with it.
        ObjectNode entry = entry("program");
        ObjectNode verb = (ObjectNode) entry.get("lexemes").get(1);
        ((ObjectNode) verb.get("senses").get(0)).put("definition", "to write the instructions a computer follows");
        ((ArrayNode) verb.get("senses")).remove(2);
        verb.putArray("retired").addObject().put("id", dropped.toString()).put("definition", "to make a person");
        ((ArrayNode) entry.get("lexemes")).remove(0);
        verb.putArray("relations");
        publishOne(publication(entry, meta("program")));

        LexemeView rewritten = lookup("en", "program").lexemes().getFirst();
        assertThat(rewritten.senses())
                .extracting(SenseView::key)
                .startsWith(kept)
                .doesNotContain(dropped);
        assertThat(jdbc.queryForList(
                        "select version from lexicon.entry_version where language = 'en' and headword = 'program'"
                                + " order by version",
                        Integer.class))
                .containsExactly(1, 2);

        SenseHistoryResponse keptHistory = senseHistory.history(kept);
        assertThat(keptHistory.versions())
                .extracting(SenseHistoryResponse.SenseVersion::version)
                .containsExactly(1, 2);
        assertThat(keptHistory.versions().get(1).sense())
                .containsEntry("pos", "VERB")
                .containsEntry("definition", "to write the instructions a computer follows");
        assertThat(keptHistory.versions().get(1).retiredTowards()).isNull();
        for (UUID gone : List.of(dropped, ofNoun)) {
            SenseHistoryResponse history = senseHistory.history(gone);
            assertThat(history.versions())
                    .extracting(SenseHistoryResponse.SenseVersion::version)
                    .containsExactly(1, 2);
            assertThat(history.versions().get(0).sense()).containsKey("definition");
            assertThat(history.versions().get(0).retiredTowards()).isNull();
            assertThat(history.versions().get(1).sense()).isNull();
            assertThat(history.versions().get(1).retiredTowards()).isEmpty();
        }
    }

    @Test
    void aWordSaidAlikeIsAHomophoneUnlessItIsTheSameWordSpelledAnotherWay() {
        publishOne(publication("antenna"));
        publishOne(publication(saidLikeAntenna("antenner"), meta("antenna")));
        publishOne(publication(saidLikeAntenna("antena"), meta("antenna")));
        // Glosses shared in two reader languages make it a spelling of antenna, not another word.
        for (String word : List.of("antenna", "antena")) {
            UUID sense = jdbc.queryForObject(
                    "select s.id from lexicon.sense s join lexicon.lexeme l on l.id = s.lexeme_id"
                            + " where l.lemma = ? and s.position = 1",
                    UUID.class,
                    word);
            for (String[] gloss : new String[][] {{"uk", "антена"}, {"de", "Antenne"}}) {
                jdbc.update(
                        "insert into lexicon.sense_gloss (id, sense_id, fluent_language, gloss, rank)"
                                + " values (gen_random_uuid(), ?, ?, ?, 1)",
                        sense,
                        gloss[0],
                        gloss[1]);
            }
        }

        LexiconEntryResponse antenna = lookup("en", "antenna");
        assertThat(antenna.lexemes().getFirst().homophones()).singleElement().satisfies(homophone -> {
            assertThat(homophone.lemma()).isEqualTo("antenner");
            assertThat(homophone.varieties()).containsExactly("en-GB", "en-US");
        });
        assertThat(lookup("en", "antennae").lexemes().getFirst().homophones())
                .as("reached through a form, so not said like the lemma")
                .isEmpty();
    }

    /** The antenna entry under another spelling, as an entry of its own: its own ids, and no forms of antenna. */
    private ObjectNode saidLikeAntenna(String spelling) {
        ObjectNode entry = entry("antenna");
        entry.put("headword", spelling);
        ObjectNode lexeme = (ObjectNode) entry.get("lexemes").get(0);
        lexeme.put("lemma", spelling);
        lexeme.put(
                "id",
                UUID.nameUUIDFromBytes(spelling.getBytes(StandardCharsets.UTF_8))
                        .toString());
        lexeme.remove("forms");
        int number = 0;
        for (JsonNode sense : lexeme.get("senses")) {
            ((ObjectNode) sense)
                    .put(
                            "id",
                            UUID.nameUUIDFromBytes((spelling + number++).getBytes(StandardCharsets.UTF_8))
                                    .toString());
        }
        return entry;
    }

    @Test
    void aLexemeTheEntryNoLongerListsIsDeletedAndAChangedTranscriptionIsProvisionalAgain() {
        publishOne(publication("program"));
        jdbc.update("update lexicon.pronunciation set status = 'verified'");
        ObjectNode entry = entry("program");
        ((ArrayNode) entry.get("lexemes")).remove(0);
        ObjectNode verb = (ObjectNode) entry.get("lexemes").get(0);
        ((ObjectNode) verb.get("pronunciations").get(0)).put("ipa", "/ˈpɹoʊ.ɡɹəm/");
        // The transcription of "programming" is dropped, so its rows must go while the lexeme's own stay.
        ((ObjectNode) verb.get("forms").get(1)).remove("ipa");

        publishOne(publication(entry, meta("program")));

        LexiconEntryResponse program = lookup("en", "program");
        assertThat(program.lexemes()).extracting(LexemeView::pos).containsExactly("VERB");
        assertThat(program.lexemes().getFirst().relations().getFirst().lexemeId())
                .isNull();
        assertThat(program.lexemes().getFirst().pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation -> pronunciation.variety() + " " + pronunciation.status()))
                .containsExactlyInAnyOrder("en-US provisional", "en-GB verified");
        assertThat(program.lexemes().getFirst().pronunciations()).hasSize(4);
        assertThat(counts().get("lexeme")).isEqualTo(1);
        assertThat(counts().get("pronunciation")).isEqualTo(4);
    }

    @Test
    void aRejectedHeadwordIsRecordedUntilAnEntryWithLexemesArrives() {
        publishOne(publication("antenna"));
        ObjectNode rejection = objectMapper.createObjectNode();
        rejection.put("schema", "4");
        rejection.put("language", "en");
        rejection.put("headword", "antennae");
        rejection.putArray("lexemes");
        rejection.put("reject", "inflected form of antenna");
        rejection.set("source", entry("antenna").get("source"));

        EntryPublicationResult result = publishOne(publication(rejection, meta("antenna")));

        assertThat(result.accepted()).isTrue();
        assertThat(result.lexemes()).isZero();
        assertThat(jdbc.queryForObject(
                        "select reason from lexicon.rejected_headword where language = 'en' and headword = 'antennae'",
                        String.class))
                .isEqualTo("inflected form of antenna");
        assertThat(lookup("en", "antennae").lexemes().getFirst().lemma()).isEqualTo("antenna");

        ObjectNode entry = entry("antenna");
        entry.put("headword", "antennae");
        publishOne(publication(entry, meta("antenna")));
        assertThat(jdbc.queryForObject("select count(*) from lexicon.rejected_headword", Integer.class))
                .isZero();
        assertThat(lookup("en", "antenna").lexemes().getFirst().headword()).isEqualTo("antennae");
    }

    @Test
    void aVarietyThatSaysAWordTwoWaysKeepsBothRankedAndInPlace() {
        ObjectNode entry = entry("antenna");
        ArrayNode spoken = (ArrayNode) entry.get("lexemes").get(0).get("pronunciations");
        spoken.insert(1, objectMapper.createObjectNode().put("variety", "en-US").put("ipa", "/æn.ˈtæn.ə/"));
        publishOne(publication(entry, meta("antenna")));
        Set<UUID> pronunciations = pronunciationIds();

        publishOne(publication(entry, meta("antenna")));

        LexemeView antenna = lookup("en", "antenna").lexemes().getFirst();
        assertThat(antenna.pronunciations().stream()
                        .filter(pronunciation -> pronunciation.formId() == null)
                        .map(pronunciation ->
                                pronunciation.variety() + "#" + pronunciation.rank() + " " + pronunciation.phonemic()))
                .containsExactly("en-GB#1 /æn.ˈten.ə/", "en-US#1 /æn.ˈtɛn.ə/", "en-US#2 /æn.ˈtæn.ə/");
        assertThat(pronunciationIds()).isEqualTo(pronunciations);
    }

    @Test
    void aClipIsStoredOnceServedForeverAndShowsUpOnItsPronunciation() {
        publishOne(publication("antenna"));
        List<UnvoicedPronunciation> unvoiced = withoutClips("en");
        assertThat(unvoiced).hasSize(4);
        assertThat(unvoiced).allSatisfy(pronunciation -> {
            assertThat(pronunciation.headword()).isEqualTo("antenna");
            assertThat(pronunciation.lemma()).isEqualTo("antenna");
            assertThat(pronunciation.pos()).isEqualTo("NOUN");
        });
        assertThat(unvoiced.stream().filter(pronunciation -> pronunciation.formId() != null))
                .extracting(UnvoicedPronunciation::form)
                .containsOnly("antennae");
        UnvoicedPronunciation british = unvoiced.stream()
                .filter(pronunciation -> pronunciation.formId() == null
                        && pronunciation.variety().equals("en-GB"))
                .findFirst()
                .orElseThrow();
        assertThat(british.phonemic()).isEqualTo("/æn.ˈten.ə/");
        assertThat(british.engineInput()).isNull();
        byte[] mp3 = {(byte) 0xFF, (byte) 0xFB, (byte) 0x90, 0x64, 1, 2, 3};

        // An unverified take first: it is kept, with what was heard, but gives the lexeme no audio capability.
        ClipSubmissionResult doubtful = putClip(british.id(), clip(mp3, "Puck", "unverified", "broke"));
        assertThat(doubtful.stored()).isTrue();
        assertThat(lookup("en", "antenna").lexemes().getFirst().capabilities().ipaAudio())
                .isFalse();

        ClipSubmissionResult first = putClip(british.id(), clip(mp3, "Charon", "verified", null));
        assertThat(first.stored()).isTrue();
        assertThat(first.objectKey()).startsWith("audio/en-GB/Charon/").endsWith(".mp3");
        assertThat(first.clipId()).isNotEqualTo(doubtful.clipId());

        LexemeView antenna = lookup("en", "antenna").lexemes().getFirst();
        PronunciationView spoken = antenna.pronunciations().stream()
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
        assertThat(antenna.capabilities().ipaAudio()).isTrue();
        assertThat(antenna.pronunciations().stream()
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
        publishOne(publication("antenna"));
        UnvoicedPronunciation british = withoutClips("en").stream()
                .filter(pronunciation -> pronunciation.formId() == null
                        && pronunciation.variety().equals("en-GB"))
                .findFirst()
                .orElseThrow();
        byte[] mp3 = {(byte) 0xFF, (byte) 0xFB, (byte) 0x90, 0x64, 1, 2, 3};
        putClip(british.id(), clip(mp3, "Puck", "unverified", "broke"));
        ClipSubmissionResult verified = putClip(british.id(), clip(mp3, "Charon", "verified", null));

        ResponseEntity<DiscoverLookupResponse> response =
                discovering.lookup(Language.EN, Language.UK, "Antenna", "A new antenna.");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        DiscoverLookupResponse sheet = response.getBody();
        assertThat(sheet.provider()).isEqualTo("lexicon");
        assertThat(sheet.entry()).isEqualTo("Antenna");
        assertThat(sheet.sourceContext()).isEqualTo("A new antenna.");
        assertThat(sheet.pronunciations())
                .extracting(pronunciation -> pronunciation.variety() + " " + pronunciation.ipa())
                .containsExactly("en-GB /æn.ˈten.ə/", "en-US /æn.ˈtɛn.ə/");
        assertThat(sheet.pronunciations().getFirst().clipUrl()).isEqualTo("/public/lexicon/clips/" + verified.clipId());
        assertThat(sheet.pronunciations().getFirst().clipStatus()).isEqualTo("verified");
        assertThat(sheet.pronunciations().get(1).clipUrl()).isNull();
        assertThat(sheet.senses()).extracting(sense -> sense.key()).containsExactly(RADIO_SENSE, INSECT_SENSE);
        assertThat(sheet.senses().getFirst()).satisfies(sense -> {
            assertThat(sense.index()).isEqualTo(1);
            assertThat(sense.headword()).isEqualTo("antenna");
            assertThat(sense.partOfSpeech()).isEqualTo("NOUN");
            assertThat(sense.transcription()).isEqualTo("/æn.ˈten.ə/");
            // An entry has the definition in its own language; glosses come with a pass of their own.
            assertThat(sense.translations()).isEmpty();
            assertThat(sense.definition()).startsWith("a metal rod or wire");
            assertThat(sense.cefr()).isEqualTo("B2");
            assertThat(sense.examples()).singleElement().satisfies(example -> {
                assertThat(example.text()).isEqualTo("We put a new antenna on the roof to improve reception.");
                assertThat(example.translation()).isNull();
            });
        });
        verifyNoInteractions(languageProcessor);
    }

    @Test
    void theSheetCarriesTheWordsBehindItsSensesAndWhatASenseIsSaidWith() {
        publishOne(publication("program"));

        DiscoverLookupResponse sheet =
                discovering.lookup(Language.EN, Language.UK, "program", null).getBody();

        assertThat(sheet.lexemes())
                .extracting(lexeme -> lexeme.index() + " " + lexeme.partOfSpeech())
                .containsExactly("1 NOUN", "2 VERB");
        assertThat(sheet.senses()).extracting(sense -> sense.lexeme()).containsExactly(1, 1, 1, 1, 2, 2, 2);
        var noun = sheet.lexemes().get(0);
        assertThat(noun.grammar()).containsEntry("countability", "Count");
        assertThat(noun.spellings())
                .extracting(spelling -> spelling.get("text"))
                .containsExactly("program", "programme");
        var verb = sheet.lexemes().get(1);
        assertThat(verb.forms()).extracting(form -> form.form()).containsExactly("programmed", "programming");
        assertThat(verb.forms().getFirst().features()).containsEntry("Tense", "Past");
        assertThat(verb.forms().getFirst().senses()).isEmpty();
        assertThat(verb.relations()).singleElement().satisfies(relation -> {
            assertThat(relation.type()).isEqualTo("derived_from");
            assertThat(relation.lemma()).isEqualTo("program");
            assertThat(relation.partOfSpeech()).isEqualTo("NOUN");
        });
        var broadcast = sheet.senses().get(0);
        assertThat(broadcast.frames()).containsExactly("a program about sth");
        assertThat(broadcast.collocations()).containsExactly("a program on TV");
        assertThat(broadcast.labels()).isEmpty();
        assertThat(sheet.senses().get(2).labels()).containsEntry("domain", "computing");
        assertThat(sheet.senses().get(4).grammar()).containsEntry("valency", "Both");
        assertThat(sheet.senses().get(6).labels()).containsEntry("figurative", true);

        publishOne(publication("antenna"));
        DiscoverLookupResponse antenna =
                discovering.lookup(Language.EN, Language.UK, "antenna", null).getBody();
        // A form that belongs to one sense names it by the sheet's number.
        assertThat(antenna.lexemes().getFirst().forms()).singleElement().satisfies(form -> {
            assertThat(form.form()).isEqualTo("antennae");
            assertThat(form.senses()).containsExactly(2);
        });
    }

    @Test
    void standaloneMissResolvesAcrossLanguagesByHeadwordInflectedFormAndAlias() {
        publishBatch(publication("antenna"), publication("program"));

        DiscoverLookupResponse byHeadword =
                discovering.lookup(Language.FR, Language.UK, "antenna", null).getBody();
        assertThat(byHeadword.language()).isEqualTo(Language.EN);
        assertThat(byHeadword.requestedLanguage()).isEqualTo(Language.FR);
        assertThat(byHeadword.senses()).hasSize(2);

        DiscoverLookupResponse byForm =
                discovering.lookup(Language.FR, Language.UK, "antennae", null).getBody();
        assertThat(byForm.language()).isEqualTo(Language.EN);
        assertThat(byForm.requestedLanguage()).isEqualTo(Language.FR);
        assertThat(byForm.senses()).extracting(sense -> sense.headword()).containsOnly("antenna");

        DiscoverLookupResponse byAlias =
                discovering.lookup(Language.DE, Language.UK, "programme", null).getBody();
        assertThat(byAlias.language()).isEqualTo(Language.EN);
        assertThat(byAlias.requestedLanguage()).isEqualTo(Language.DE);
        assertThat(byAlias.lexemes()).extracting(lexeme -> lexeme.lemma()).containsOnly("program");
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

    private List<LexiconSuggestion> suggestions(String language, String prefix, String fluent) {
        ResponseEntity<List<LexiconSuggestion>> response = reading.suggestions(language, prefix, fluent, 6);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody();
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
                        "sense_example",
                        "lexeme_relation",
                        "entry_alias",
                        "lexicon_build")
                .stream()
                .collect(Collectors.toMap(
                        table -> table,
                        table -> jdbc.queryForObject("select count(*) from lexicon." + table, Integer.class)));
    }

    private static List<UUID> senseKeys(LexiconEntryResponse entry) {
        return entry.lexemes().stream()
                .flatMap(lexeme -> lexeme.senses().stream())
                .map(SenseView::key)
                .toList();
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

package com.almonium.analyzer.analyzer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.analyzer.dto.DiscoverExampleResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverPronunciationResponse;
import com.almonium.analyzer.analyzer.mapper.DiscoverLexiconMapper;
import com.almonium.analyzer.translator.dto.DefinitionDto;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.dto.TranslationDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.Capabilities;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ClipView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.ExampleView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.FormView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.GlossView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.LexemeView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.PronunciationView;
import com.almonium.learning.lexicon.dto.response.LexiconEntryResponse.SenseView;
import com.almonium.learning.lexicon.service.LexiconLookupService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscoverLookupServiceTest {
    @Mock
    LanguageProcessor languageProcessor;

    @Mock
    FrequencyService frequencyService;

    @Mock
    LexiconLookupService lexiconLookupService;

    DiscoverLookupService service;

    @BeforeEach
    void setUp() {
        service = new DiscoverLookupService(
                languageProcessor, frequencyService, lexiconLookupService, new DiscoverLexiconMapper());
    }

    @Test
    void returnsStructuredSensesFrequencyEvidenceAndContext() {
        TranslationCardDto translation = TranslationCardDto.builder()
                .provider("dictionary")
                .definitions(new DefinitionDto[] {
                    DefinitionDto.builder()
                            .text("die Ausgabe")
                            .pos("noun")
                            .transcription("aʊsɡaːbə")
                            .translations(new TranslationDto[] {
                                TranslationDto.builder().text("edition").build(),
                                TranslationDto.builder().text("issue").build()
                            })
                            .build()
                })
                .build();
        when(lexiconLookupService.lookup("de", "Ausgabe")).thenReturn(Optional.empty());
        when(languageProcessor.translate("Ausgabe", Language.DE, Language.EN)).thenReturn(translation);
        when(frequencyService.getFrequency(Language.DE, "Ausgabe")).thenReturn(Optional.of(64));

        var result = service.lookup("  Ausgabe  ", " Die zweite Ausgabe erschien. ", Language.DE, Language.EN);

        assertThat(result.entry()).isEqualTo("Ausgabe");
        assertThat(result.sourceContext()).isEqualTo("Die zweite Ausgabe erschien.");
        assertThat(result.provider()).isEqualTo("dictionary");
        assertThat(result.frequency().band()).isEqualTo("Common in books");
        assertThat(result.frequency().provenance()).contains("ngrams.dev", "ger corpus");
        assertThat(result.pronunciations()).isEmpty();
        assertThat(result.senses()).singleElement().satisfies(sense -> {
            assertThat(sense.index()).isEqualTo(1);
            assertThat(sense.translations()).containsExactly("edition", "issue");
            assertThat(sense.definition()).isNull();
            assertThat(sense.cefr()).isNull();
            assertThat(sense.examples()).isEmpty();
        });
    }

    @Test
    void degradesToAnEmptySheetWhenExternalProvidersAreUnavailable() {
        when(lexiconLookupService.lookup("de", "Ausgabe")).thenReturn(Optional.empty());
        when(languageProcessor.translate("Ausgabe", Language.DE, Language.EN))
                .thenThrow(new IllegalStateException("translator unavailable"));
        when(frequencyService.getFrequency(Language.DE, "Ausgabe"))
                .thenThrow(new IllegalStateException("ngrams unavailable"));

        var result = service.lookup("Ausgabe", null, Language.DE, Language.EN);

        assertThat(result.provider()).isNull();
        assertThat(result.frequency()).isNull();
        assertThat(result.senses()).isEmpty();
        assertThat(result.pronunciations()).isEmpty();
    }

    @Test
    void servesTheLexiconWhenItHasTheWordAndLeavesTheProviderAlone() {
        UUID verifiedClip = UUID.randomUUID();
        UUID doubtfulClip = UUID.randomUUID();
        UUID pluralForm = UUID.randomUUID();
        PronunciationView british = pronunciation(
                null,
                "en-GB",
                1,
                "/ˈbləʊk/",
                List.of(clip(verifiedClip, "verified"), clip(doubtfulClip, "unverified")));
        PronunciationView american = pronunciation(null, "en-US", 1, "/ˈbloʊk/", List.of());
        PronunciationView americanAgain =
                pronunciation(null, "en-US", 2, "/ˈblɔʊk/", List.of(clip(doubtfulClip, "unverified")));
        PronunciationView plural = pronunciation(pluralForm, "en-GB", 1, "/ˈbləʊks/", List.of());
        SenseView man = sense(
                1,
                "a man, especially an ordinary one",
                "B2",
                Map.of(
                        "uk",
                        List.of(new GlossView("чолов'яга", false, null), new GlossView("мужик", true, null)),
                        "de",
                        List.of(new GlossView("Kerl", true, null))),
                List.of(new ExampleView(
                        "He seems like a nice bloke.", "generated", Map.of("uk", "Він гарний мужик."))));
        SenseView boyfriend = sense(2, "a woman's partner", null, Map.of(), List.of());
        LexemeView noun = lexeme("bloke", "NOUN", List.of(american, british, americanAgain, plural), List.of(man));
        LexemeView verb = lexeme(
                "bloke", "VERB", List.of(pronunciation(null, "en-GB", 1, "/bləʊk/", List.of())), List.of(boyfriend));
        when(lexiconLookupService.lookup("en", "bloke"))
                .thenReturn(Optional.of(new LexiconEntryResponse("en", "bloke", List.of(noun, verb))));
        when(frequencyService.getFrequency(Language.EN, "bloke")).thenReturn(Optional.of(40));

        var result = service.lookup("bloke", "A nice bloke.", Language.EN, Language.UK);

        assertThat(result.provider()).isEqualTo("lexicon");
        assertThat(result.frequency().score()).isEqualTo(40);
        assertThat(result.senses()).hasSize(2);
        assertThat(result.senses().get(0)).satisfies(sense -> {
            assertThat(sense.index()).isEqualTo(1);
            assertThat(sense.headword()).isEqualTo("bloke");
            assertThat(sense.partOfSpeech()).isEqualTo("NOUN");
            assertThat(sense.transcription()).isEqualTo("/ˈbloʊk/");
            assertThat(sense.translations()).containsExactly("мужик", "чолов'яга");
            assertThat(sense.definition()).isEqualTo("a man, especially an ordinary one");
            assertThat(sense.cefr()).isEqualTo("B2");
            assertThat(sense.examples())
                    .containsExactly(new DiscoverExampleResponse("He seems like a nice bloke.", "Він гарний мужик."));
        });
        assertThat(result.senses().get(1)).satisfies(sense -> {
            assertThat(sense.index()).isEqualTo(2);
            assertThat(sense.partOfSpeech()).isEqualTo("VERB");
            assertThat(sense.translations()).isEmpty();
            assertThat(sense.cefr()).isNull();
        });
        assertThat(result.pronunciations())
                .containsExactly(
                        new DiscoverPronunciationResponse(
                                "en-GB", "/ˈbləʊk/", "/public/lexicon/clips/" + verifiedClip, "verified"),
                        new DiscoverPronunciationResponse("en-GB", "/bləʊk/", null, null),
                        new DiscoverPronunciationResponse("en-US", "/ˈbloʊk/", null, null),
                        new DiscoverPronunciationResponse(
                                "en-US", "/ˈblɔʊk/", "/public/lexicon/clips/" + doubtfulClip, "unverified"));
        verify(languageProcessor, never()).translate(anyString(), any(), any());
    }

    @Test
    void anExampleWithoutARenderingInTheTranslationLanguageKeepsItsText() {
        SenseView sense = sense(
                1,
                "to laugh quietly",
                "B1",
                Map.of("de", List.of(new GlossView("leise lachen", null, null))),
                List.of(new ExampleView("She chuckled.", "generated", null)));
        LexemeView verb = lexeme("chuckle", "VERB", List.of(), List.of(sense));
        when(lexiconLookupService.lookup("en", "chuckle"))
                .thenReturn(Optional.of(new LexiconEntryResponse("en", "chuckle", List.of(verb))));
        when(frequencyService.getFrequency(Language.EN, "chuckle")).thenReturn(Optional.empty());

        var result = service.lookup("chuckle", null, Language.EN, Language.DE);

        assertThat(result.frequency()).isNull();
        assertThat(result.pronunciations()).isEmpty();
        assertThat(result.senses()).singleElement().satisfies(found -> {
            assertThat(found.transcription()).isNull();
            assertThat(found.translations()).containsExactly("leise lachen");
            assertThat(found.examples()).containsExactly(new DiscoverExampleResponse("She chuckled.", null));
        });
    }

    private static LexemeView lexeme(
            String headword, String pos, List<PronunciationView> pronunciations, List<SenseView> senses) {
        return new LexemeView(
                UUID.randomUUID(),
                headword,
                headword,
                pos,
                1,
                "word",
                Map.of(),
                null,
                null,
                "provisional",
                null,
                List.of(new FormView(UUID.randomUUID(), headword, Map.of())),
                pronunciations,
                senses,
                List.of(),
                List.of(),
                new Capabilities(true, true, !pronunciations.isEmpty(), false, false, false),
                null);
    }

    private static PronunciationView pronunciation(
            UUID formId, String variety, int rank, String ipa, List<ClipView> clips) {
        return new PronunciationView(UUID.randomUUID(), formId, variety, rank, ipa, "provisional", clips);
    }

    private static ClipView clip(UUID id, String status) {
        return new ClipView(
                id,
                "google",
                "Charon",
                new BigDecimal("1.00"),
                "mp3_44100_128",
                640,
                status,
                "/public/lexicon/clips/" + id);
    }

    private static SenseView sense(
            int position,
            String definition,
            String cefr,
            Map<String, List<GlossView>> glosses,
            List<ExampleView> examples) {
        return new SenseView(
                UUID.randomUUID(),
                position,
                definition,
                cefr,
                "writer",
                null,
                null,
                null,
                null,
                glosses,
                examples,
                List.of());
    }
}

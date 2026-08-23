package com.almonium.analyzer.analyzer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.dto.DefinitionDto;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.dto.TranslationDto;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscoverLookupServiceTest {
    @Mock
    LanguageProcessor languageProcessor;

    @Mock
    FrequencyService frequencyService;

    @InjectMocks
    DiscoverLookupService service;

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
        when(languageProcessor.translate("Ausgabe", Language.DE, Language.EN)).thenReturn(translation);
        when(frequencyService.getFrequency(Language.DE, "Ausgabe")).thenReturn(Optional.of(64));

        var result = service.lookup("  Ausgabe  ", " Die zweite Ausgabe erschien. ", Language.DE, Language.EN);

        assertThat(result.entry()).isEqualTo("Ausgabe");
        assertThat(result.sourceContext()).isEqualTo("Die zweite Ausgabe erschien.");
        assertThat(result.frequency().band()).isEqualTo("Common in books");
        assertThat(result.frequency().provenance()).contains("ngrams.dev", "ger corpus");
        assertThat(result.senses()).singleElement().satisfies(sense -> {
            assertThat(sense.index()).isEqualTo(1);
            assertThat(sense.translations()).containsExactly("edition", "issue");
        });
    }

    @Test
    void degradesToAnEmptySheetWhenExternalProvidersAreUnavailable() {
        when(languageProcessor.translate("Ausgabe", Language.DE, Language.EN))
                .thenThrow(new IllegalStateException("translator unavailable"));
        when(frequencyService.getFrequency(Language.DE, "Ausgabe"))
                .thenThrow(new IllegalStateException("ngrams unavailable"));

        var result = service.lookup("Ausgabe", null, Language.DE, Language.EN);

        assertThat(result.provider()).isNull();
        assertThat(result.frequency()).isNull();
        assertThat(result.senses()).isEmpty();
    }
}

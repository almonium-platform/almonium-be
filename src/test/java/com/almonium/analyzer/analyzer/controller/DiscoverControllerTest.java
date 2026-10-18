package com.almonium.analyzer.analyzer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.almonium.analyzer.analyzer.controller.open.DiscoverController;
import com.almonium.analyzer.analyzer.dto.DiscoverExampleResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverFrequencyResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverLookupResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverPronunciationResponse;
import com.almonium.analyzer.analyzer.dto.DiscoverSenseResponse;
import com.almonium.analyzer.analyzer.service.DiscoverLookupService;
import com.almonium.analyzer.analyzer.service.FrequencyService;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class DiscoverControllerTest {
    @Mock
    FrequencyService frequencyService;

    @Mock
    DiscoverLookupService lookupService;

    MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new DiscoverController(frequencyService, lookupService))
                .build();
    }

    @Test
    void rendersTheLexiconSheetWithPronunciationsDefinitionsAndExamples() throws Exception {
        when(lookupService.lookup("bloke", "A nice bloke.", Language.EN, Language.UK))
                .thenReturn(new DiscoverLookupResponse(
                        "bloke",
                        "A nice bloke.",
                        Language.EN,
                        Language.UK,
                        "lexicon",
                        new DiscoverFrequencyResponse(40, "Less common in books", "ngrams.dev"),
                        List.of(new DiscoverSenseResponse(
                                1,
                                "bloke",
                                "NOUN",
                                "/ˈbləʊk/",
                                List.of("мужик"),
                                "a man, especially an ordinary one",
                                "B2",
                                List.of(new DiscoverExampleResponse("He seems like a nice bloke.", null)))),
                        List.of(
                                new DiscoverPronunciationResponse(
                                        "en-GB", "/ˈbləʊk/", "/public/lexicon/clips/c1", "verified"),
                                new DiscoverPronunciationResponse("en-US", "/ˈbloʊk/", null, null))));

        mvc.perform(get("/public/discover/lookup/EN/UK").param("entry", "bloke").param("context", "A nice bloke."))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entry").value("bloke"))
                .andExpect(jsonPath("$.sourceContext").value("A nice bloke."))
                .andExpect(jsonPath("$.language").value("EN"))
                .andExpect(jsonPath("$.translationLanguage").value("UK"))
                .andExpect(jsonPath("$.provider").value("lexicon"))
                .andExpect(jsonPath("$.frequency.score").value(40))
                .andExpect(jsonPath("$.senses.length()").value(1))
                .andExpect(jsonPath("$.senses[0].index").value(1))
                .andExpect(jsonPath("$.senses[0].headword").value("bloke"))
                .andExpect(jsonPath("$.senses[0].partOfSpeech").value("NOUN"))
                .andExpect(jsonPath("$.senses[0].transcription").value("/ˈbləʊk/"))
                .andExpect(jsonPath("$.senses[0].translations[0]").value("мужик"))
                .andExpect(jsonPath("$.senses[0].definition").value("a man, especially an ordinary one"))
                .andExpect(jsonPath("$.senses[0].cefr").value("B2"))
                .andExpect(jsonPath("$.senses[0].examples[0].text").value("He seems like a nice bloke."))
                .andExpect(jsonPath("$.senses[0].examples[0].translation").doesNotExist())
                .andExpect(jsonPath("$.pronunciations.length()").value(2))
                .andExpect(jsonPath("$.pronunciations[0].variety").value("en-GB"))
                .andExpect(jsonPath("$.pronunciations[0].ipa").value("/ˈbləʊk/"))
                .andExpect(jsonPath("$.pronunciations[0].clipUrl").value("/public/lexicon/clips/c1"))
                .andExpect(jsonPath("$.pronunciations[0].clipStatus").value("verified"))
                .andExpect(jsonPath("$.pronunciations[1].variety").value("en-US"))
                .andExpect(jsonPath("$.pronunciations[1].clipUrl").doesNotExist())
                .andExpect(jsonPath("$.pronunciations[1].clipStatus").doesNotExist());
    }

    @Test
    void keepsTheProviderShapeWithTheNewFieldsEmpty() throws Exception {
        when(lookupService.lookup("Ausgabe", null, Language.DE, Language.EN))
                .thenReturn(new DiscoverLookupResponse(
                        "Ausgabe",
                        null,
                        Language.DE,
                        Language.EN,
                        "dictionary",
                        null,
                        List.of(new DiscoverSenseResponse(
                                1, "die Ausgabe", "noun", "aʊsɡaːbə", List.of("edition"), null, null, List.of())),
                        List.of()));

        mvc.perform(get("/public/discover/lookup/DE/EN").param("entry", "Ausgabe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.provider").value("dictionary"))
                .andExpect(jsonPath("$.frequency").doesNotExist())
                .andExpect(jsonPath("$.senses[0].translations[0]").value("edition"))
                .andExpect(jsonPath("$.senses[0].definition").doesNotExist())
                .andExpect(jsonPath("$.senses[0].examples.length()").value(0))
                .andExpect(jsonPath("$.pronunciations.length()").value(0));
    }
}

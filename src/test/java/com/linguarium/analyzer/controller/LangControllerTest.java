package com.linguarium.analyzer.controller;

import com.google.protobuf.ByteString;
import com.linguarium.BaseControllerTest;
import com.linguarium.analyzer.dto.AnalysisDto;
import com.linguarium.analyzer.service.impl.LanguageProcessor;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.service.CardService;
import com.linguarium.client.words.dto.WordsReportDto;
import com.linguarium.translator.dto.MLTranslationCard;
import com.linguarium.translator.dto.TranslationCardDto;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;
import com.linguarium.util.TestDataGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LangController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class LangControllerTest extends BaseControllerTest {
    static final String BASE_URL = "/api/lang/";

    static final String TRANSLATE_URL = BASE_URL + "translate/{langFrom}/{langTo}/{text}";
    static final String REPORT_URL = BASE_URL + "words/{text}/{lang}/report";
    static final String BULK_PRONOUNCE_URL = BASE_URL + "words/{text}/audio/{lang}";
    static final String RANDOM_URL = BASE_URL + "words/random";
    static final String BULK_TRANSLATE_URL = BASE_URL + "translations/{langTo}/bulk";
    static final String SEARCH_URL = BASE_URL + "cards/search/{text}";

    @MockBean
    CardService cardService;

    @MockBean
    LanguageProcessor languageProcessor;

    LocalUser principal;
    Learner learner;

    @BeforeEach
    public void setUp() {
        principal = TestDataGenerator.createLocalUser();
        learner = principal.getUser().getLearner();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should find cards by search text when called with valid text")
    @WithMockUser(username = "user@example.com")
    @Test
    public void givenSearchEntryAndUser_whenSearchByEntry_thenReturnMatchingCards() throws Exception {
        // Arrange
        String searchText = "hello";
        CardDto[] dummyCards = TestDataGenerator.createCardDtos();
        when(cardService.searchByEntry(eq(searchText), eq(learner))).thenReturn(List.of(dummyCards));

        // Act & Assert
        mockMvc.perform(get(SEARCH_URL, searchText)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dummyCards)));

        // Verify
        verify(cardService).searchByEntry(eq(searchText), eq(learner));
    }

    @DisplayName("Should translate text from one language to another")
    @Test
    public void shouldTranslateText() throws Exception {
        // Arrange
        String langFrom = Language.EN.name();
        String langTo = Language.ES.name();
        String text = "Hello";

        TranslationCardDto translationCardDto = TestDataGenerator.createTranslationCardDto();

        when(languageProcessor.translate(text, Language.EN, Language.ES))
                .thenReturn(translationCardDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(TRANSLATE_URL, langFrom, langTo, text)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal)))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(translationCardDto)));
    }

    @DisplayName("Should get a report for a given text and language")
    @Test
    public void shouldGetReportForTextAndLanguage() throws Exception {
        // Arrange
        String text = "Hello";
        String lang = Language.EN.name();
        AnalysisDto analysisDto = TestDataGenerator.createTestAnalysisDto();
        when(languageProcessor.getReport(text, lang, principal.getUser().getLearner()))
                .thenReturn(analysisDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(LangControllerTest.REPORT_URL, text, lang)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(analysisDto)));
    }

    @DisplayName("Should bulk translate text to a specified language")
    @Test
    public void shouldBulkTranslateText() throws Exception {
        // Arrange
        String langTo = Language.ES.name();
        String text = "Hello";

        MLTranslationCard mlTranslationCard = TestDataGenerator.createMLTranslationCard();

        when(languageProcessor.bulkTranslate(text, Language.ES))
                .thenReturn(mlTranslationCard);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post(BULK_TRANSLATE_URL, langTo)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(text))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mlTranslationCard)));
    }

    @DisplayName("Should bulk pronounce text to a specified language")
    @Test
    public void shouldBulkPronounceText() throws Exception {
        // Arrange
        String lang = Language.EN.name();
        String text = "Hello";

        ByteString audioBytes = TestDataGenerator.generateRandomAudioBytes();
        when(languageProcessor.textToSpeech(lang, text))
                .thenReturn(audioBytes);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(BULK_PRONOUNCE_URL, text, lang)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=file.mp3"))
                .andExpect(MockMvcResultMatchers.content().bytes(audioBytes.toByteArray()));
    }

    @DisplayName("Should get a random WordsReportDto")
    @Test
    public void shouldGetRandomWordsReportDto() throws Exception {
        // Arrange
        WordsReportDto wordsReportDto = TestDataGenerator.createEmptyWordsReportDto();
        when(languageProcessor.getRandom()).thenReturn(wordsReportDto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(RANDOM_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wordsReportDto)));
    }
}

package com.linguarium.suggestion.controller;

import com.linguarium.BaseControllerTest;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.suggestion.dto.CardSuggestionDto;
import com.linguarium.suggestion.service.CardSuggestionService;
import com.linguarium.user.model.Learner;
import com.linguarium.util.TestDataGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardSuggestionController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class CardSuggestionControllerTest extends BaseControllerTest {

    static final String BASE_URL = "/api/cards/suggestions";
    static final String ACCEPT_CARD_URL = BASE_URL + "/{id}/accept";
    static final String DECLINE_CARD_URL = BASE_URL + "/{id}/decline";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CardSuggestionService cardSuggestionService;

    Learner learner;

    @BeforeEach
    void setUp() {
        LocalUser principal = TestDataGenerator.createLocalUser();
        learner = principal.getUser().getLearner();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should suggest card")
    @Test
    void givenCardSuggestionDto_whenSuggestCard_thenCardSuggestionCreated() throws Exception {
        CardSuggestionDto dto = CardSuggestionDto.builder()
                .cardId(1L)
                .recipientId(2L)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @DisplayName("Should accept a card suggestion for current user")
    @Test
    void givenCardId_whenAcceptCard_thenSuggestionAccepted() throws Exception {
        Long cardIdToAccept = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put(ACCEPT_CARD_URL, cardIdToAccept)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cardSuggestionService).acceptSuggestion(cardIdToAccept, learner);
    }

    @DisplayName("Should decline a card suggestion for current user")
    @Test
    void givenCardId_whenDeclineCard_thenSuggestionDeclined() throws Exception {
        Long cardIdToDecline = 1L;

        mockMvc.perform(MockMvcRequestBuilders.put(DECLINE_CARD_URL, cardIdToDecline)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cardSuggestionService).declineSuggestion(cardIdToDecline, learner);
    }


    @DisplayName("Should retrieve suggested cards for a user")
    @Test
    void givenUser_whenGetSuggestedCards_thenReturnsCards() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk());

        verify(cardSuggestionService).getSuggestedCards(eq(learner));
    }
}

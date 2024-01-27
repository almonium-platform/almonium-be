package com.linguarium.card.controller;

import com.linguarium.base.BaseControllerTest;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardCreationDto;
import com.linguarium.card.dto.CardUpdateDto;
import com.linguarium.card.service.CardService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest extends BaseControllerTest {
    static final String BASE_URL = "/cards";
    static final String ID_PLACEHOLDER = "/{id}";

    static final String CREATE_CARD_URL = BASE_URL;
    static final String UPDATE_CARD_URL = BASE_URL + ID_PLACEHOLDER;
    static final String GET_CARDS_URL = BASE_URL;
    static final String GET_CARDS_OF_LANG_URL = BASE_URL + "/lang/{code}";
    static final String GET_CARD_URL = BASE_URL + ID_PLACEHOLDER;
    static final String DELETE_CARD_URL = BASE_URL + ID_PLACEHOLDER;

    @MockBean
    CardService cardService;

    LocalUser principal;

    @BeforeEach
    void setUp() {
        principal = TestDataGenerator.createLocalUser();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should create card")
    @Test
    void givenCardCreationDto_whenCreateCard_thenCreatedSuccessfully() throws Exception {
        LocalUser localUser = TestDataGenerator.createLocalUser();
        CardCreationDto dto = TestDataGenerator.getCardCreationDto();

        mockMvc.perform(post(CREATE_CARD_URL)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isCreated());
    }

    @DisplayName("Should update card")
    @Test
    void givenCardUpdateDto_whenUpdateCard_thenUpdateSuccessfully() throws Exception {
        Long cardId = 1L;
        CardUpdateDto updateDto = TestDataGenerator.generateRandomCardUpdateDto();

        mockMvc.perform(put(UPDATE_CARD_URL, cardId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
                .andExpect(status().isOk());

        verify(cardService).updateCard(eq(cardId), any(CardUpdateDto.class), any(Learner.class));
    }

    @DisplayName("Should retrieve all cards of a user")
    @Test
    void givenUser_whenGetCards_thenReturnsCards() throws Exception {
        mockMvc.perform(get(GET_CARDS_URL)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
                .andExpect(status().isOk());

        verify(cardService).getUsersCards(any(Learner.class));
    }

    @DisplayName("Should retrieve all cards of a user for a specific language")
    @Test
    void givenUserAndLanguageCode_whenGetCardsOfLang_thenReturnsCards() throws Exception {
        String languageCode = Language.EN.name();

        mockMvc.perform(get(GET_CARDS_OF_LANG_URL, languageCode)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
                .andExpect(status().isOk());

        verify(cardService).getUsersCardsOfLang(eq(languageCode), any(Learner.class));
    }

    @DisplayName("Should retrieve a card by ID")
    @Test
    void givenCardId_whenGetCard_thenReturnsCard() throws Exception {
        Long cardId = 1L;

        mockMvc.perform(get(GET_CARD_URL, cardId)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
                .andExpect(status().isOk());

        verify(cardService).getCardById(cardId);
    }

    @DisplayName("Should delete a card by ID")
    @Test
    void givenCardId_whenDeleteCard_thenCardIsDeleted() throws Exception {
        Long cardId = 1L;

        mockMvc.perform(delete(DELETE_CARD_URL, cardId)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
                .andExpect(status().isNoContent());

        verify(cardService).deleteById(cardId);
    }
}

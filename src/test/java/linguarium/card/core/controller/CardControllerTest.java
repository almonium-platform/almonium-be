package linguarium.card.core.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import linguarium.base.BaseControllerTest;
import linguarium.card.core.dto.CardCreationDto;
import linguarium.card.core.dto.CardUpdateDto;
import linguarium.card.core.service.CardService;
import linguarium.engine.translator.model.Language;
import linguarium.user.core.model.Learner;
import linguarium.user.core.model.User;
import linguarium.util.GeneralUtils;
import linguarium.util.TestDataGenerator;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;

@WebMvcTest(CardController.class)
@FieldDefaults(level = PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest extends BaseControllerTest {
    private static final String BASE_URL = "/cards";
    private static final String ID_PLACEHOLDER = "/{id}";
    private static final String CREATE_CARD_URL = BASE_URL;
    private static final String UPDATE_CARD_URL = BASE_URL + ID_PLACEHOLDER;
    private static final String GET_CARDS_URL = BASE_URL;
    private static final String GET_CARDS_BY_HASH_URL = BASE_URL + "/public" + ID_PLACEHOLDER;
    private static final String GET_CARDS_OF_LANG_URL = BASE_URL + "/lang/{code}";
    private static final String GET_CARD_URL = BASE_URL + ID_PLACEHOLDER;
    private static final String DELETE_CARD_URL = BASE_URL + ID_PLACEHOLDER;

    @MockBean
    CardService cardService;

    @BeforeEach
    void setUp() {
        User principal = TestDataGenerator.buildTestUserWithId();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should create card")
    @Test
    @SneakyThrows
    void givenCardCreationDto_whenCreateCard_thenCreatedSuccessfully() {
        CardCreationDto dto = TestDataGenerator.getCardCreationDto();

        mockMvc.perform(post(CREATE_CARD_URL)
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @DisplayName("Should update card")
    @Test
    @SneakyThrows
    void givenCardUpdateDto_whenUpdateCard_thenUpdateSuccessfully() {
        Long cardId = 1L;
        CardUpdateDto updateDto = TestDataGenerator.generateRandomCardUpdateDto();

        mockMvc.perform(put(UPDATE_CARD_URL, cardId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(cardService).updateCard(eq(cardId), any(CardUpdateDto.class), any(Learner.class));
    }

    @DisplayName("Should retrieve all cards of a user")
    @Test
    @SneakyThrows
    void givenUser_whenGetCards_thenReturnsCards() {
        mockMvc.perform(get(GET_CARDS_URL)).andExpect(status().isOk());

        verify(cardService).getUsersCards(any(Learner.class));
    }

    @DisplayName("Should retrieve all cards of a user for a specific language")
    @Test
    @SneakyThrows
    void givenUserAndLanguageCode_whenGetCardsOfLang_thenReturnsCards() {
        String languageCode = Language.EN.name();

        mockMvc.perform(get(GET_CARDS_OF_LANG_URL, languageCode)).andExpect(status().isOk());

        verify(cardService).getUsersCardsOfLang(eq(languageCode), any(Learner.class));
    }

    @DisplayName("Should retrieve a card by ID")
    @Test
    @SneakyThrows
    void givenCardId_whenGetCard_thenReturnsCard() {
        Long cardId = 1L;

        mockMvc.perform(get(GET_CARD_URL, cardId)).andExpect(status().isOk());

        verify(cardService).getCardById(cardId);
    }

    @DisplayName("Should retrieve a card by hash")
    @Test
    @SneakyThrows
    void givenCardHash_whenGetCardByHash_thenReturnsCard() {
        String hash = GeneralUtils.generateId();

        mockMvc.perform(get(GET_CARDS_BY_HASH_URL, hash)).andExpect(status().isOk());

        verify(cardService).getCardByPublicId(hash);
    }

    @DisplayName("Should delete a card by ID")
    @Test
    @SneakyThrows
    void givenCardId_whenDeleteCard_thenCardIsDeleted() {
        Long cardId = 1L;

        mockMvc.perform(delete(DELETE_CARD_URL, cardId)).andExpect(status().isNoContent());

        verify(cardService).deleteById(cardId);
    }
}

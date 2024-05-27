package linguarium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import linguarium.base.BaseControllerTest;
import linguarium.engine.translator.model.enums.Language;
import linguarium.user.core.dto.LanguageUpdateRequest;
import linguarium.user.core.dto.UserInfo;
import linguarium.user.core.dto.UsernameAvailability;
import linguarium.user.core.dto.UsernameUpdateRequest;
import linguarium.user.core.model.entity.User;
import linguarium.user.core.service.LearnerService;
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

@WebMvcTest(UserController.class)
@FieldDefaults(level = PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest extends BaseControllerTest {
    private static final String BASE_URL = "/users";
    private static final String ME_URL = BASE_URL + "/me";
    private static final String CHECK_USERNAME_AVAILABILITY_URL = BASE_URL + "/{username}/availability/";
    private static final String UPDATE_USERNAME_URL = ME_URL + "/username";
    private static final String DELETE_CURRENT_USER_ACCOUNT_URL = ME_URL + "/account";
    private static final String UPDATE_TARGET_LANGUAGES_URL = ME_URL + "/target-langs";
    private static final String UPDATE_FLUENT_LANGUAGES_URL = ME_URL + "/fluent-langs";

    @MockBean
    LearnerService learnerService;

    User user;

    @BeforeEach
    void setUp() {
        user = TestDataGenerator.buildTestUserWithId();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(user));
    }

    @DisplayName("Should return current user info when requested by authenticated user")
    @Test
    @SneakyThrows
    void givenAuthenticatedUser_whenGetCurrentUser_thenReturnUserInfo() {
        UserInfo testUserInfo = createTestUserInfo();

        when(userService.buildUserInfoFromUser(any(User.class))).thenReturn(testUserInfo);

        mockMvc.perform(get(ME_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserInfo)));
    }

    @DisplayName("Should verify username availability")
    @Test
    @SneakyThrows
    void givenUsername_whenCheckUsernameAvailability_thenRespondWithAvailabilityStatus() {
        String username = user.getUsername();
        boolean isAvailable = true;
        UsernameAvailability usernameAvailability = new UsernameAvailability(isAvailable);

        when(userService.isUsernameAvailable(username)).thenReturn(isAvailable);

        mockMvc.perform(get(CHECK_USERNAME_AVAILABILITY_URL, username).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(usernameAvailability)));

        verify(userService).isUsernameAvailable(username);
    }

    @DisplayName("Should update username for current user")
    @Test
    @SneakyThrows
    void givenUsernameUpdateRequest_whenUpdateUsername_thenUpdateSuccessfully() {
        UsernameUpdateRequest request = new UsernameUpdateRequest("newUsername");

        mockMvc.perform(put(UPDATE_USERNAME_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(userService).changeUsernameById(request.newUsername(), user.getId());
    }

    @DisplayName("Should delete current user account")
    @Test
    @SneakyThrows
    void givenCurrentUser_whenDeleteCurrentUserAccount_thenAccountDeleted() {
        mockMvc.perform(delete(DELETE_CURRENT_USER_ACCOUNT_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService).deleteAccount(user);
    }

    @DisplayName("Should update target languages for current user")
    @Test
    @SneakyThrows
    void givenLanguageUpdateRequest_whenUpdateTargetLanguages_thenUpdateSuccessfully() {
        LanguageUpdateRequest request = new LanguageUpdateRequest(List.of(Language.EN, Language.ES));

        mockMvc.perform(put(UPDATE_TARGET_LANGUAGES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(learnerService).updateTargetLanguages(request.langCodes(), user.getLearner());
    }

    @DisplayName("Should update fluent languages for current user")
    @Test
    @SneakyThrows
    void givenLanguageUpdateRequest_whenUpdateFluentLanguages_thenUpdateSuccessfully() {
        LanguageUpdateRequest request = new LanguageUpdateRequest(List.of(Language.FR, Language.DE));

        mockMvc.perform(put(UPDATE_FLUENT_LANGUAGES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(learnerService).updateFluentLanguages(request.langCodes(), user.getLearner());
    }

    private UserInfo createTestUserInfo() {
        String id = "testuser";
        String username = "testuser";
        String email = "test@example.com";
        Language uiLang = Language.EN;
        String profilePicLink = "https://example.com/profilepic.jpg";
        String background = "https://example.com/background.jpg";
        Integer streak = 0;
        Collection<Language> targetLangs = Arrays.asList(Language.EN, Language.ES);
        Collection<Language> fluentLangs = Arrays.asList(Language.FR, Language.DE);
        Collection<String> tags = Arrays.asList("tag1", "tag2");

        return new UserInfo(
                id, username, email, uiLang, profilePicLink, background, streak, targetLangs, fluentLangs, tags);
    }
}

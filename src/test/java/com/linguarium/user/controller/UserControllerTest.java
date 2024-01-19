package com.linguarium.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguarium.auth.dto.UserInfo;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.configuration.security.PasswordEncoder;
import com.linguarium.configuration.security.jwt.TokenProvider;
import com.linguarium.configuration.security.oauth2.CustomOAuth2UserService;
import com.linguarium.configuration.security.oauth2.CustomOidcUserService;
import com.linguarium.configuration.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.linguarium.configuration.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.linguarium.translator.model.Language;
import com.linguarium.user.dto.LanguageUpdateRequest;
import com.linguarium.user.dto.UsernameAvailability;
import com.linguarium.user.dto.UsernameUpdateRequest;
import com.linguarium.user.model.User;
import com.linguarium.user.service.LearnerService;
import com.linguarium.user.service.UserService;
import com.linguarium.user.service.impl.LocalUserDetailServiceImpl;
import com.linguarium.util.TestEntityGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserControllerTest {
    static final String ME_URL = "/api/users/me";
    static final String CHECK_USERNAME_AVAILABILITY_URL = "/api/users/{username}/availability/";
    static final String UPDATE_USERNAME_URL = "/api/users/me/username";
    static final String DELETE_CURRENT_USER_ACCOUNT_URL = "/api/users/me/account";
    static final String UPDATE_TARGET_LANGUAGES_URL = "/api/users/me/target-languages";
    static final String UPDATE_FLUENT_LANGUAGES_URL = "/api/users/me/fluent-languages";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    LearnerService learnerService;

    @MockBean
    LocalUserDetailServiceImpl localUserDetailsService;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    CustomOidcUserService customOidcUserService;

    @MockBean
    OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @MockBean
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    LocalUser localUser;

    @MockBean
    User user;

    UserInfo testUserInfo;

    @BeforeEach
    void setUp() {
        when(localUser.getUser()).thenReturn(user);
    }

    @DisplayName("Should return current user info when requested by authenticated user")
    @Test
    void givenAuthenticatedUser_whenGetCurrentUser_thenReturnUserInfo() throws Exception {
        testUserInfo = createTestUserInfo();
        LocalUser localUser = TestEntityGenerator.createLocalUser();

        when(userService.buildUserInfo(any(User.class))).thenReturn(testUserInfo);

        mockMvc.perform(MockMvcRequestBuilders.get(ME_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(TestEntityGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUserInfo)));
    }

    @DisplayName("Should verify username availability")
    @Test
    void givenUsername_whenCheckUsernameAvailability_thenRespondWithAvailabilityStatus() throws Exception {
        String username = "testuser";
        boolean isAvailable = true;
        UsernameAvailability usernameAvailability = new UsernameAvailability(isAvailable);

        when(userService.isUsernameAvailable(username)).thenReturn(isAvailable);

        mockMvc.perform(get(CHECK_USERNAME_AVAILABILITY_URL, username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(usernameAvailability)));

        verify(userService).isUsernameAvailable(username);
    }

    @DisplayName("Should update username for current user")
    @Test
    void givenUsernameUpdateRequest_whenUpdateUsername_thenUpdateSuccessfully() throws Exception {
        UsernameUpdateRequest request = new UsernameUpdateRequest("newUsername");

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_USERNAME_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(TestEntityGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isNoContent());

        verify(userService).changeUsername(request.getNewUsername(), user.getId());
    }

    @DisplayName("Should delete current user account")
    @Test
    void givenCurrentUser_whenDeleteCurrentUserAccount_thenAccountDeleted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_CURRENT_USER_ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(authentication(TestEntityGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isNoContent());

        verify(userService).deleteAccount(user);
    }

    @DisplayName("Should update target languages for current user")
    @Test
    void givenLanguageUpdateRequest_whenUpdateTargetLanguages_thenUpdateSuccessfully() throws Exception {
        LanguageUpdateRequest request = new LanguageUpdateRequest(List.of(Language.EN.name(), Language.ES.name()));

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_TARGET_LANGUAGES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(TestEntityGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isNoContent());

        verify(learnerService).updateTargetLanguages(request.getLangCodes(), user.getLearner());
    }

    @DisplayName("Should update fluent languages for current user")
    @Test
    void givenLanguageUpdateRequest_whenUpdateFluentLanguages_thenUpdateSuccessfully() throws Exception {
        LanguageUpdateRequest request = new LanguageUpdateRequest(List.of(Language.FR.name(), Language.DE.name()));

        mockMvc.perform(MockMvcRequestBuilders.put(UPDATE_FLUENT_LANGUAGES_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(authentication(TestEntityGenerator.getAuthenticationToken(localUser)))
                )
                .andExpect(status().isNoContent());

        verify(learnerService).updateFluentLanguages(request.getLangCodes(), user.getLearner());
    }

    private UserInfo createTestUserInfo() {
        String id = "testuser";
        String username = "testuser";
        String email = "test@example.com";
        String uiLang = "EN";
        String profilePicLink = "https://example.com/profilepic.jpg";
        String background = "https://example.com/background.jpg";
        Integer streak = 0;
        Collection<String> targetLangs = Arrays.asList("EN", "ES");
        Collection<String> fluentLangs = Arrays.asList("FR", "DE");
        Collection<String> tags = Arrays.asList("tag1", "tag2");

        return new UserInfo(id, username, email, uiLang, profilePicLink, background, streak, targetLangs, fluentLangs, tags);
    }
}
package com.linguarium.friendship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.configuration.security.PasswordEncoder;
import com.linguarium.configuration.security.jwt.TokenProvider;
import com.linguarium.configuration.security.oauth2.CustomOAuth2UserService;
import com.linguarium.configuration.security.oauth2.CustomOidcUserService;
import com.linguarium.configuration.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.linguarium.configuration.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.linguarium.friendship.dto.FriendInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.service.FriendshipService;
import com.linguarium.user.model.User;
import com.linguarium.user.service.impl.LocalUserDetailServiceImpl;
import com.linguarium.util.TestDataGenerator;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class FriendControllerTest {
    static final String BASE_URL = "/api/friends";
    static final String FRIEND_URL = BASE_URL;

    static final String GET_MY_FRIENDS_URL = FRIEND_URL;
    static final String SEARCH_FRIENDS_BY_EMAIL_URL = FRIEND_URL + "/search";
    static final String MANAGE_FRIENDSHIP_URL = FRIEND_URL + "/friendships";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FriendshipService friendshipService;

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

    @BeforeEach
    void setUp() {
        when(localUser.getUser()).thenReturn(user);
    }

    @DisplayName("Should retrieve current user's friends")
    @Test
    void givenCurrentUser_whenGetMyFriends_thenReturnFriendsList() throws Exception {
        List<FriendInfoDto> friendsList = TestDataGenerator.generateFriendInfoDtoList(5);

        when(friendshipService.getFriends(anyLong())).thenReturn(friendsList);

        mockMvc.perform(get(GET_MY_FRIENDS_URL)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendsList)));
    }

    @DisplayName("Should find friend by email")
    @Test
    void givenEmail_whenSearchFriendsByEmail_thenFriendShouldBePresentAndUsernameShouldMatch() throws Exception {
        String email = "test@example.com";
        FriendInfoDto friendInfo = TestDataGenerator.generateFriendInfoDto();
        when(friendshipService.findFriendByEmail(email)).thenReturn(Optional.of(friendInfo));

        mockMvc.perform(get(SEARCH_FRIENDS_BY_EMAIL_URL)
                        .param("email", email)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendInfo)));
    }

    @DisplayName("Should manage friendship based on action")
    @Test
    void givenFriendshipActionDto_whenManageFriendship_thenPerformAction() throws Exception {
        FriendshipActionDto dto = TestDataGenerator.generateFriendshipActionDto();
        Friendship friendship = TestDataGenerator.generateFriendship(1L, 2L);
        when(friendshipService.manageFriendship(dto)).thenReturn(friendship);

        mockMvc.perform(post(MANAGE_FRIENDSHIP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(authentication(TestDataGenerator.getAuthenticationToken(localUser))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendship)));
    }
}

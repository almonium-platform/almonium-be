package com.linguarium.friendship.controller;

import com.linguarium.base.BaseControllerTest;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.friendship.dto.FriendshipInfoDto;
import com.linguarium.friendship.dto.FriendshipActionDto;
import com.linguarium.friendship.model.Friendship;
import com.linguarium.friendship.service.FriendshipService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendshipController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class FriendshipControllerTest extends BaseControllerTest {
    static final String BASE_URL = "/friends";
    static final String FRIEND_URL = BASE_URL;

    static final String GET_MY_FRIENDS_URL = FRIEND_URL;
    static final String SEARCH_FRIENDS_BY_EMAIL_URL = FRIEND_URL + "/search";
    static final String MANAGE_FRIENDSHIP_URL = FRIEND_URL + "/friendships";

    @MockBean
    FriendshipService friendshipService;

    LocalUser principal;

    @BeforeEach
    void setUp() {
        principal = TestDataGenerator.createLocalUser();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should retrieve current user's friends")
    @Test
    void givenCurrentUser_whenGetMyFriends_thenReturnFriendsList() throws Exception {
        List<FriendshipInfoDto> friendsList = TestDataGenerator.generateFriendInfoDtoList(5);

        when(friendshipService.getFriends(anyLong())).thenReturn(friendsList);

        mockMvc.perform(get(GET_MY_FRIENDS_URL)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendsList)));
    }

    @DisplayName("Should find friend by email")
    @Test
    void givenEmail_whenSearchFriendsByEmail_thenFriendShouldBePresentAndUsernameShouldMatch() throws Exception {
        String email = "testEmail";
        FriendshipInfoDto friendInfo = TestDataGenerator.generateFriendInfoDto();
        when(friendshipService.findFriendByEmail(email)).thenReturn(Optional.of(friendInfo));

        mockMvc.perform(get(SEARCH_FRIENDS_BY_EMAIL_URL)
                        .param("email", email)
                        .with(authentication(TestDataGenerator.getAuthenticationToken(principal))))
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
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendship)));
    }
}

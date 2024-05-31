package linguarium.user.friendship.controller;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import linguarium.auth.common.entity.Principal;
import linguarium.base.BaseControllerTest;
import linguarium.user.core.model.entity.User;
import linguarium.user.friendship.dto.FriendDto;
import linguarium.user.friendship.model.entity.Friendship;
import linguarium.user.friendship.model.enums.FriendshipAction;
import linguarium.user.friendship.service.FriendshipService;
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

@WebMvcTest(FriendshipController.class)
@FieldDefaults(level = PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class FriendshipControllerTest extends BaseControllerTest {
    private static final String BASE_URL = "/friendships";
    private static final String GET_MY_FRIENDS_URL = BASE_URL;
    private static final String MANAGE_FRIENDSHIP_URL = BASE_URL + "/{id}";
    private static final String SEARCH_FRIENDS_BY_EMAIL_URL = BASE_URL + "/search";

    @MockBean
    FriendshipService friendshipService;

    @BeforeEach
    void setUp() {
        Principal principal = TestDataGenerator.buildTestPrincipal();
        SecurityContextHolder.getContext().setAuthentication(TestDataGenerator.getAuthenticationToken(principal));
    }

    @DisplayName("Should retrieve current user's friends")
    @Test
    @SneakyThrows
    void givenCurrentUser_whenGetMyFriends_thenReturnFriendsList() {
        List<FriendDto> friendsList = TestDataGenerator.generateFriendInfoDtoList(5);

        when(friendshipService.getFriends(anyLong())).thenReturn(friendsList);

        mockMvc.perform(get(GET_MY_FRIENDS_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendsList)));
    }

    @DisplayName("Should find friend by email")
    @Test
    @SneakyThrows
    void givenEmail_whenSearchFriendsByEmail_thenFriendShouldBePresentAndUsernameShouldMatch() {
        String email = "testEmail";
        FriendDto friendInfo = TestDataGenerator.generateFriendInfoDto();
        when(friendshipService.findFriendByEmail(email)).thenReturn(Optional.of(friendInfo));

        mockMvc.perform(get(SEARCH_FRIENDS_BY_EMAIL_URL).param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendInfo)));
    }

    @DisplayName("Should manage friendship based on action")
    @Test
    @SneakyThrows
    void givenFriendshipActionDto_whenManageFriendship_thenPerformAction() {
        Long id = 1L;
        FriendshipAction dto = FriendshipAction.CANCEL;
        Friendship friendship = TestDataGenerator.generateFriendship(1L, 2L);
        User user = TestDataGenerator.buildTestUserWithId();
        when(friendshipService.manageFriendship(user, id, dto)).thenReturn(friendship);

        mockMvc.perform(patch(MANAGE_FRIENDSHIP_URL, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(friendship)));
    }
}

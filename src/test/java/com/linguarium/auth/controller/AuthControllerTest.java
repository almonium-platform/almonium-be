package com.linguarium.auth.controller;

import com.linguarium.auth.dto.SocialProvider;
import com.linguarium.auth.dto.request.LoginRequest;
import com.linguarium.auth.dto.request.SignUpRequest;
import com.linguarium.auth.exception.UserAlreadyExistsAuthenticationException;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.base.BaseControllerTest;
import com.linguarium.configuration.GlobalExceptionHandler;
import com.linguarium.friendship.service.FriendshipService;
import com.linguarium.user.model.Profile;
import com.linguarium.user.service.ProfileService;
import com.linguarium.user.service.UserService;
import com.linguarium.util.TestDataGenerator;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class, includeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = GlobalExceptionHandler.class)
})
@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends BaseControllerTest {
    private static final String BASE_URL = "/auth";
    private static final String LOGIN_URL = BASE_URL + "/login";
    private static final String REGISTER_URL = BASE_URL + "/register";

    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    UserService userService;
    @MockBean
    ProfileService profileService;
    @MockBean
    FriendshipService friendshipService;

    @DisplayName("Should authenticate user successfully")
    @Test
    void givenValidCredentials_whenLogin_thenReturnJwtToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password");
        LocalUser localUser = TestDataGenerator.createLocalUser();
        String jwt = "mockJwtToken";

        when(authenticationManager.authenticate(any())).thenReturn(new TestingAuthenticationToken(localUser, null));
        when(tokenProvider.createToken(any())).thenReturn(jwt);

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(jwt));

        verify(profileService).updateLoginStreak(any(Profile.class));
        verify(tokenProvider).createToken(any(Authentication.class));
    }

    @DisplayName("Should handle authentication failure")
    @Test
    void givenInvalidCredentials_whenLogin_thenReturnUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "wrongpassword");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("Should register user successfully")
    @Test
    void givenValidSignUpRequest_whenRegister_thenSuccess() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @DisplayName("Should handle existing user registration attempt")
    @Test
    void givenExistingUser_whenRegister_thenBadRequest() throws Exception {
        SignUpRequest signUpRequest = createSignUpRequest();

        doThrow(new UserAlreadyExistsAuthenticationException("User already exists"))
                .when(userService).registerNewUser(any(SignUpRequest.class));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    private SignUpRequest createSignUpRequest() {
        return SignUpRequest.builder()
                .userID(1L)
                .providerUserId("dummyProviderUserId")
                .username("dummyUsername")
                .email("dummy@example.com")
                .socialProvider(SocialProvider.FACEBOOK)
                .profilePicLink("http://example.com/dummy.jpg")
                .password("dummyPassword123")
                .build();
    }
}

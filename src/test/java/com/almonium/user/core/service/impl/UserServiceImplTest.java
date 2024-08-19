package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.subscription.model.entity.PlanSubscription;
import com.almonium.subscription.service.PlanSubscriptionService;
import com.almonium.subscription.service.StripeApiService;
import com.almonium.user.core.mapper.UserMapper;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PlanSubscriptionService planSubscriptionService;

    @Mock
    StripeApiService stripeApiService;

    @Mock
    UserMapper userMapper;

    @DisplayName("Should change username")
    @Test
    void givenUsername_whenChangeUsername_thenUsernameByIdChanged() {
        String username = "newUsername";
        long id = 1L;

        userService.changeUsernameById(username, id);

        verify(userRepository).changeUsername(username, id);
    }

    @DisplayName("Should return user optional for existing user")
    @Test
    void givenExistingUser_whenFindUserById_thenReturnUserOptional() {
        Long userId = 1L;
        User user = UserUtility.getUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getById(userId);

        assertThat(result).isEqualTo(user);
        verify(userRepository).findById(userId);
    }

    @DisplayName("Should return empty optional for non existing user")
    @Test
    void givenNonExistingUser_whenFindUserById_thenThrowEntityNotFoundException() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(userId)).isInstanceOf(EntityNotFoundException.class);
        verify(userRepository).findById(userId);
    }

    @DisplayName("Should delete user account")
    @Test
    void givenUser_whenDeleteAccount_thenRepositoryDeleteIsCalled() {
        User user = UserUtility.getUser();
        when(planSubscriptionService.findActiveSubscription(user)).thenReturn(Optional.empty());
        userService.deleteAccount(user);

        verify(userRepository).delete(user);
    }

    @DisplayName("Should delete user account")
    @Test
    void givenUserWithActiveSubscription_whenDeleteAccount_thenRepositoryDeleteIsCalledAndSubscriptionCancelled() {
        User user = UserUtility.getUser();
        String subscriptionId = "sub_123";
        when(planSubscriptionService.findActiveSubscription(user))
                .thenReturn(Optional.of(PlanSubscription.builder()
                        .stripeSubscriptionId(subscriptionId)
                        .user(user)
                        .build()));
        userService.deleteAccount(user);

        verify(stripeApiService).cancelSubscription(subscriptionId);
        verify(userRepository).delete(user);
    }

    @DisplayName("Should return user if email exists")
    @Test
    void givenExistentEmail_whenFindByEmail_thenReturnUser() {
        String email = "john@example.com";
        User expectedUser = UserUtility.getUser();
        expectedUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findByEmail(email).orElseThrow();

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @DisplayName("Should return null if email doesn't exist")
    @Test
    void givenNonExistentEmail_whenFindByEmail_thenReturnNull() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> actualUser = userService.findByEmail(email);

        assertThat(actualUser).isEmpty();
    }

    @DisplayName("Should use mapper to build userInfo")
    @Test
    void givenLocalUser_whenBuildUserInfo_thenInvokeMapper() {
        User user = UserUtility.getUser();
        userService.buildUserInfoFromUser(user);
        verify(userMapper).userToUserInfo(user);
    }

    @DisplayName("Should return true when username is available")
    @Test
    void givenAvailableUsername_whenIsUsernameAvailable_thenReturnsTrue() {
        // Arrange
        String username = "newUsername";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = userService.isUsernameAvailable(username);

        // Assert
        assertThat(result).isTrue();
    }

    @DisplayName("Should return false when username is already taken")
    @Test
    void givenTakenUsername_whenIsUsernameAvailable_thenReturnsFalse() {
        // Arrange
        String username = "existingUsername";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act
        boolean result = userService.isUsernameAvailable(username);

        // Assert
        assertThat(result).isFalse();
    }
}

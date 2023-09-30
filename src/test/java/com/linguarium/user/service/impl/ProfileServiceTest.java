package com.linguarium.user.service.impl;

import com.linguarium.user.model.Profile;
import com.linguarium.user.repository.ProfileRepository;
import com.linguarium.user.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @InjectMocks
    ProfileServiceImpl profileService;
    @Mock
    ProfileRepository profileRepository;

    @BeforeEach
    void setUp() {
        profileService = new ProfileServiceImpl(profileRepository);
    }

    @Test
    @DisplayName("Should increase streak if last login is on the previous day")
    void givenLastLoginIsPreviousDay_whenUpdateLoginStreak_thenStreakIsIncreased() {
        Profile profile = new Profile();
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(1);
        profile.setLastLogin(lastLogin);
        profile.setStreak(5);
        profileService.updateLoginStreak(profile);

        assertThat(profile.getStreak()).isEqualTo(6);
        assertThat(profile.getLastLogin().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("Should reset streak if last login is not on the previous day")
    void givenLastLoginNotPreviousDay_whenUpdateLoginStreak_thenStreakIsReset() {
        Profile profile = new Profile();
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(2);
        profile.setLastLogin(lastLogin);
        profile.setStreak(5);

        profileService.updateLoginStreak(profile);

        assertThat(profile.getStreak()).isEqualTo(1);
        assertThat(profile.getLastLogin().toLocalDate()).isEqualTo(LocalDate.now());
    }
}

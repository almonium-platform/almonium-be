package com.linguarium.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.linguarium.user.model.Profile;
import com.linguarium.user.repository.ProfileRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ProfileServiceTest {
    @InjectMocks
    ProfileServiceImpl profileService;
    @Mock
    ProfileRepository profileRepository;

    @DisplayName("Should increase streak if last login is on the previous day")
    @Test
    void givenLastLoginIsPreviousDay_whenUpdateLoginStreak_thenStreakIsIncreased() {
        Profile profile = new Profile();
        LocalDateTime lastLogin = LocalDateTime.now().minusDays(1);
        profile.setLastLogin(lastLogin);
        profile.setStreak(5);
        profileService.updateLoginStreak(profile);

        assertThat(profile.getStreak()).isEqualTo(6);
        assertThat(profile.getLastLogin().toLocalDate()).isEqualTo(LocalDate.now());
    }

    @DisplayName("Should reset streak if last login is not on the previous day")
    @Test
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

package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.service.ProfileService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = PRIVATE)
class ProfileServiceTest {

    @Mock
    ProfileRepository profileRepository;

    @InjectMocks
    ProfileService profileService;

    @DisplayName("Should stamp the current time as the last login")
    @Test
    void givenStaleLastLogin_whenUpdateLastLogin_thenLastLoginIsStampedNow() {
        Profile profile = new Profile();
        profile.setLastLogin(LocalDateTime.now().minusDays(2));

        profileService.updateLastLogin(profile);

        assertThat(profile.getLastLogin().toLocalDate()).isEqualTo(LocalDate.now());
        verify(profileRepository).save(profile);
    }

    @Test
    void socialEmailsAreOnUntilTheMemberTurnsThemOff() {
        Profile profile = Profile.builder().id(UUID.randomUUID()).build();
        when(profileRepository.findById(profile.getId())).thenReturn(Optional.of(profile));
        assertThat(profile.isSocialEmailNotifications()).isTrue();

        profileService.updateSocialEmailNotifications(profile.getId(), false);

        assertThat(profile.isSocialEmailNotifications()).isFalse();
        verify(profileRepository).save(profile);
    }
}

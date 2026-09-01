package com.almonium.user.core.service.impl;

import static lombok.AccessLevel.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.almonium.user.core.model.entity.Profile;
import com.almonium.user.core.repository.ProfileRepository;
import com.almonium.user.core.service.ProfileService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
}

package com.almonium.user.core.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.user.core.repository.ProfileRepository;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

@ExtendWith(MockitoExtension.class)
class LastSeenRecorderTest {
    @Mock
    ProfileRepository profileRepository;

    @InjectMocks
    LastSeenRecorder recorder;

    @Test
    void stampsOncePerIntervalPerUser() {
        UUID user = UUID.randomUUID();
        UUID other = UUID.randomUUID();

        recorder.touch(user);
        recorder.touch(user);
        recorder.touch(other);

        verify(profileRepository, times(1)).stampLastSeen(eq(user), any(Instant.class));
        verify(profileRepository, times(1)).stampLastSeen(eq(other), any(Instant.class));
    }

    @Test
    void aFailedStampIsForgottenSoTheNextRequestTriesAgain() {
        UUID user = UUID.randomUUID();
        when(profileRepository.stampLastSeen(eq(user), any()))
                .thenThrow(new DataAccessResourceFailureException("down"))
                .thenReturn(1);

        recorder.touch(user);
        recorder.touch(user);

        verify(profileRepository, times(2)).stampLastSeen(eq(user), any(Instant.class));
    }

    @Test
    void aFailedStampDoesNotEscape() {
        UUID user = UUID.randomUUID();
        when(profileRepository.stampLastSeen(eq(user), any()))
                .thenThrow(new DataAccessResourceFailureException("down"));

        recorder.touch(user);

        verify(profileRepository, never()).findById(any());
    }
}

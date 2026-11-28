package com.almonium.learning.play.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.play.dto.response.HigherLowerRunResult;
import com.almonium.learning.play.repository.HigherLowerBestStore;
import com.almonium.learning.play.repository.HigherLowerWordStore;
import com.almonium.user.core.model.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HigherLowerServiceTest {
    @Mock
    HigherLowerWordStore wordStore;

    @Mock
    HigherLowerBestStore bestStore;

    HigherLowerService service;
    User user;

    @BeforeEach
    void setUp() {
        service = new HigherLowerService(wordStore, bestStore);
        user = new User();
        user.setId(UUID.randomUUID());
    }

    @Test
    void asksTheLexiconInItsOwnLowerCaseCodesWithTheReadersMeaningLanguage() {
        service.words(Language.DE, Language.UK);

        verify(wordStore).sample("de", "uk", HigherLowerService.BATCH_SIZE);
    }

    @Test
    void givesEnglishMeaningsWithoutAFluentLanguageAndUkrainianOnesForEnglish() {
        service.words(Language.DE, null);
        service.words(Language.EN, null);
        service.words(Language.FR, Language.FR);

        verify(wordStore).sample("de", "en", HigherLowerService.BATCH_SIZE);
        verify(wordStore).sample("en", "uk", HigherLowerService.BATCH_SIZE);
        verify(wordStore).sample("fr", "en", HigherLowerService.BATCH_SIZE);
    }

    @Test
    void answersTheNewBestAndTheOneBeforeIt() {
        when(bestStore.lockBest(user.getId(), Language.DE)).thenReturn(Optional.of(5));

        assertThat(service.record(user, Language.DE, 6)).isEqualTo(new HigherLowerRunResult(6, 5));
        verify(bestStore).keepLonger(user.getId(), Language.DE, 6);
    }

    @Test
    void keepsTheOldBestWhenARunFallsShortAndHasNoneBeforeAFirstRun() {
        when(bestStore.lockBest(user.getId(), Language.DE)).thenReturn(Optional.of(9));
        when(bestStore.lockBest(user.getId(), Language.FR)).thenReturn(Optional.empty());

        assertThat(service.record(user, Language.DE, 3)).isEqualTo(new HigherLowerRunResult(9, 9));
        assertThat(service.record(user, Language.FR, 2)).isEqualTo(new HigherLowerRunResult(2, null));
    }
}

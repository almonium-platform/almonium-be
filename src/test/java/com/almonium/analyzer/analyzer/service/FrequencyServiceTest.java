package com.almonium.analyzer.analyzer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.almonium.analyzer.client.ngrams.adapter.NgramsAdapter;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FrequencyServiceTest {
    @Mock
    NgramsAdapter ngramsAdapter;

    @InjectMocks
    FrequencyService service;

    @Test
    void calculatesScoresForEachSupportedCorpusLanguage() {
        when(ngramsAdapter.getRelativeFrequency("Ausgabe", Language.DE)).thenReturn(Optional.of(0.000001));
        when(ngramsAdapter.getRelativeFrequency("слово", Language.RU)).thenReturn(Optional.of(0.000001));

        assertThat(service.getFrequency(Language.DE, "Ausgabe"))
                .hasValueSatisfying(score -> assertThat(score).isPositive());
        assertThat(service.getFrequency(Language.RU, "слово"))
                .hasValueSatisfying(score -> assertThat(score).isPositive());
    }
}

package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.client.ngrams.adapter.NgramsAdapter;
import com.almonium.analyzer.translator.model.enums.Language;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FrequencyService {
    private static final int LOWEST_SCORE = 1;
    private static final int EXPONENT = 9;
    private static final double FREQUENCY_THRESHOLD = Math.pow(10, -EXPONENT);
    private static final double ENGLISH_SCALE = 12.78990589161462;
    private static final Map<Language, Double> languageScale = Map.of(Language.EN, ENGLISH_SCALE);
    private final NgramsAdapter ngramsAdapter;

    public Optional<Integer> getFrequency(Language language, String input) {
        Optional<Double> reportedFrequencyOptional = ngramsAdapter.getRelativeFrequency(input, language);
        return reportedFrequencyOptional.map(aDouble -> calculateRelativeFrequency(aDouble, language));
    }

    private int calculateRelativeFrequency(double frequency, Language language) {
        if (frequency == 0) {
            return 0;
        }
        if (frequency < FREQUENCY_THRESHOLD) {
            return LOWEST_SCORE;
        }
        double normalizeByZero = Math.log10(frequency) + EXPONENT; // lowest log can get is -e, so we add e to normalize
        double result = languageScale.get(language) * normalizeByZero + LOWEST_SCORE;
        return Math.toIntExact(Math.round(result));
    }
}

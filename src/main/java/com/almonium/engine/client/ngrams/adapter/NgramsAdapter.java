package com.almonium.engine.client.ngrams.adapter;

import com.almonium.engine.client.exception.ApiNoLangSupportException;
import com.almonium.engine.client.ngrams.client.NgramsClient;
import com.almonium.engine.client.ngrams.dto.NgramsResponseDto;
import com.almonium.engine.translator.model.enums.Language;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NgramsAdapter {
    private static final String API_NAME = "ngrams";
    private static final Map<Language, String> corpusName = Map.of(
            Language.EN, "eng",
            Language.RU, "rus",
            Language.DE, "ger");

    private final NgramsClient ngramsClient;

    public Optional<Double> getRelativeFrequency(String input, Language language) {
        if (corpusName.get(language) == null) {
            throw new ApiNoLangSupportException(language, API_NAME);
        }

        NgramsResponseDto dto = ngramsClient.searchWord(corpusName.get(language), input);

        return dto.ngrams().stream().findFirst().map(NgramsResponseDto.Ngram::relTotalMatchCount);
    }
}

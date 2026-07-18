package com.almonium.analyzer.translator.provider;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.mapper.DictionaryDtoMapper;
import com.almonium.analyzer.client.exception.ApiIntegrationException;
import com.almonium.analyzer.client.yandex.YandexClient;
import com.almonium.analyzer.client.yandex.dto.YandexDto;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class YandexTranslationProvider implements TranslationProvider {
    static final String PROVIDER_NAME = "YANDEX";

    YandexClient yandexClient;
    DictionaryDtoMapper dictionaryDtoMapper;

    @Override
    public String providerName() {
        return PROVIDER_NAME;
    }

    @Override
    public TranslationCardDto translate(String entry, Language sourceLanguage, Language targetLanguage) {
        try {
            ResponseEntity<YandexDto> response = yandexClient.translate(entry, sourceLanguage, targetLanguage);
            if (response.getStatusCode() == HttpStatus.NOT_IMPLEMENTED) {
                throw unsupportedLanguagePair(sourceLanguage, targetLanguage);
            }
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ApiIntegrationException("Yandex translation request failed with " + response.getStatusCode());
            }

            TranslationCardDto card = dictionaryDtoMapper.yandexToGeneral(response.getBody());
            card.setProvider(PROVIDER_NAME);
            return card;
        } catch (HttpStatusCodeException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_IMPLEMENTED) {
                throw unsupportedLanguagePair(sourceLanguage, targetLanguage);
            }
            throw new ApiIntegrationException(
                    "Yandex translation request failed with " + exception.getStatusCode(), exception);
        }
    }

    private IllegalStateException unsupportedLanguagePair(Language sourceLanguage, Language targetLanguage) {
        return new IllegalStateException(
                "Yandex does not support configured language pair " + sourceLanguage + "-" + targetLanguage);
    }
}

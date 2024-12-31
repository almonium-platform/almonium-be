package com.almonium.analyzer.client.yandex;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.AbstractClient;
import com.almonium.analyzer.client.Client;
import com.almonium.analyzer.client.yandex.dto.YandexDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.properties.ExternalApiProperties;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;

@Client
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class YandexClient extends AbstractClient {
    private static final String URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";
    private static final String LANG = "lang";
    private static final String TEXT = "text";
    private static final String KEY = "key";

    ExternalApiProperties externalApiProperties;

    public ResponseEntity<YandexDto> translate(String word, Language from, Language to) {
        String langPair = String.format(
                "%s-%s", from.name().toLowerCase(Locale.ROOT), to.name().toLowerCase(Locale.ROOT));

        return super.request(
                URL,
                Map.of(
                        KEY, externalApiProperties.getKey().getYandex(),
                        TEXT, word,
                        LANG, langPair),
                YandexDto.class);
    }
}

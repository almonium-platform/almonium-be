package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.external_api.response.yandex.YandexDto;
import com.linguatool.model.entity.lang.Language;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.Locale;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Client
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Slf4j
public class YandexClient extends AbstractClient {
    static final String URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup";
    static final String LANG = "lang";
    static final String TEXT = "text";
    static final String KEY = "key";
    @Value("${external.api.key.yandex}")
    String KEY_VALUE;

    public ResponseEntity<YandexDto> translate(String word, Language from, Language to) {
        String langPair = String.format("%s-%s",
                from.getCode().toLowerCase(Locale.ROOT),
                to.getCode().toLowerCase(Locale.ROOT));

        return super.request(
                URL,
                Map.of(
                        KEY, KEY_VALUE,
                        TEXT, word,
                        LANG, langPair
                ),
                YandexDto.class);
    }
}

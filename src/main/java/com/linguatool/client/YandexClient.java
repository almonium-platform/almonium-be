package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.api.response.free_dictionary.FDEntry;
import com.linguatool.model.dto.api.response.yandex.YandexDto;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linguatool.util.GeneralUtils.queryBuilder;
import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class YandexClient {
    static String BASE_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/lookup/";
    static String EN_RU = "en-ru";
    static String LANG = "lang";
    static String TEXT = "text";
    static String KEY = "key";
    static String KEY_VALUE = "dict.1.1.20220409T164704Z.6923d1452886ccd4.32658382f53afdcb90780649f12fdb5bf5b77590";
    static String EN_UK = "en-uk";
    RestTemplate restTemplate;


    private ResponseEntity<YandexDto> translateToUkr(String word) {
        return request(word, EN_UK);
    }

    private ResponseEntity<YandexDto> translateToRus(String word) {
        return request(word, EN_RU);
    }

    private ResponseEntity<YandexDto> request(String word, String langPair) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL, List.of(KEY, LANG, TEXT));

        Map<String, String> params = new HashMap<>();
        params.put(KEY, KEY_VALUE);
        params.put(TEXT, word);
        params.put(LANG, langPair);

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                YandexDto.class,
                params);
    }

}

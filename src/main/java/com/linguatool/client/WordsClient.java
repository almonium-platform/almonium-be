package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.api.response.wordnik.WordnikRandomWordDto;
import com.linguatool.model.dto.api.response.words.WordsReportDto;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class WordsClient {
    static String API_ID_HEADER_NAME = "X-RapidAPI-Host";
    static String API_ID_HEADER_VALUE = "wordsapiv1.p.rapidapi.com";
    static String API_KEY_HEADER_NAME = "X-RapidAPI-Key";
    static String API_KEY_HEADER_VALUE = "7f58826f86mshccb070bd9f1dde8p1a6b3fjsnd5a086d2813c";

    static String BASE_URL = "https://wordsapiv1.p.rapidapi.com/words/";
    static String RANDOM = "random";

    RestTemplate restTemplate;

    public ResponseEntity<WordsReportDto> getReport(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, API_KEY_HEADER_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL + word,
            List.of()
        );

        Map<String, Object> params = new HashMap<>();

        return restTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            WordsReportDto.class, params);
    }

    public ResponseEntity<WordsReportDto> getRandomWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, API_KEY_HEADER_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL,
            List.of(RANDOM)
        );

        Map<String, Object> params = new HashMap<>();
        params.put(RANDOM, true);

        return restTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            WordsReportDto.class, params);
    }

}

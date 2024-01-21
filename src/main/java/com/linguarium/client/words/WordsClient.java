package com.linguarium.client.words;

import com.linguarium.client.AbstractClient;
import com.linguarium.client.Client;
import com.linguarium.client.words.dto.WordsReportDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linguarium.util.GeneralUtils.queryBuilder;
import static lombok.AccessLevel.PRIVATE;

@Client
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Slf4j
public class WordsClient extends AbstractClient {
    static final String API_ID_HEADER_NAME = "X-RapidAPI-Host";
    static final String API_ID_HEADER_VALUE = "wordsapiv1.p.rapidapi.com";
    static final String API_KEY_HEADER_NAME = "X-RapidAPI-Key";
    static final String BASE_URL = "https://wordsapiv1.p.rapidapi.com/words/";
    static final String RANDOM = "random";

    @Value("${external.api.key.yandex}")
    String API_KEY_HEADER_VALUE;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity<WordsReportDto> getReport(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, API_KEY_HEADER_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

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
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

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

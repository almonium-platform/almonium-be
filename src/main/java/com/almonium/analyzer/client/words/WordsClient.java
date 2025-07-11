package com.almonium.analyzer.client.words;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.AbstractClient;
import com.almonium.analyzer.client.Client;
import com.almonium.analyzer.client.words.dto.WordsReportDto;
import com.almonium.config.properties.ExternalApiProperties;
import com.almonium.util.GeneralUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Client
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WordsClient extends AbstractClient {
    private static final String API_ID_HEADER_NAME = "X-RapidAPI-Host";
    private static final String API_ID_HEADER_VALUE = "wordsapiv1.p.rapidapi.com";
    private static final String API_KEY_HEADER_NAME = "X-RapidAPI-Key";
    private static final String BASE_URL = "https://wordsapiv1.p.rapidapi.com/words/";
    private static final String RANDOM = "random";

    RestTemplate restTemplate;
    ExternalApiProperties externalApiProperties;

    public ResponseEntity<WordsReportDto> getReport(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, externalApiProperties.getKey().getWords());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = GeneralUtils.queryBuilder(BASE_URL + word, List.of());

        Map<String, Object> params = new HashMap<>();

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), WordsReportDto.class, params);
    }

    public ResponseEntity<WordsReportDto> getRandomWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, externalApiProperties.getKey().getWords());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = GeneralUtils.queryBuilder(BASE_URL, List.of(RANDOM));

        Map<String, Object> params = new HashMap<>();
        params.put(RANDOM, true);

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), WordsReportDto.class, params);
    }
}

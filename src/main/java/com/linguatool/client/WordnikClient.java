package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.api.response.wordnik.WordnikFrequencyDto;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class WordnikClient {
    private static final int START_YEAR = 1950;
    private static final int END_YEAR = 2022;
    static String APIKEY_HEADER_NAME = "api_key";
    static String APIKEY_HEADER_VALUE = "d6fsnd5tslptc5mnhao3qbdda6g9j2rbfw6yopzkr2dsq0gjo";
    static String BASE_URL = "https://api.wordnik.com/v4/word.json/";

    static String EXAMPLES_ENDPOINT = "/examples";
    static String DEFINITIONS = "/examples";
    static String HYPERNATION = "/hypernation";
    static String TOP_EXAMPLE = "/topExample";
    static String RELATED_WORDS = "/relatedWords";
    static String PRONUNCIATION = "/pronunciation";
    static String PHRASES = "/phrases";
    static String FREQUENCY = "/frequency";


    RestTemplate restTemplate;


    public ResponseEntity<WordnikFrequencyDto> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, APIKEY_HEADER_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(BASE_URL + word + FREQUENCY)
            .queryParam("startYear", "{startYear}")
            .queryParam("endYear", "{endYear}")
            .queryParam("useCanonical", "{useCanonical}")
            .encode()
            .toUriString();

        Map<String, Object> params = new HashMap<>();
        params.put("useCanonical", false);
        params.put("startYear", START_YEAR);
        params.put("endYear", END_YEAR);


        return restTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            WordnikFrequencyDto.class, params);
    }

}

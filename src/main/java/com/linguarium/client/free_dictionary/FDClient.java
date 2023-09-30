package com.linguarium.client.free_dictionary;

import com.linguarium.client.AbstractClient;
import com.linguarium.client.Client;
import com.linguarium.client.free_dictionary.dto.FDEntry;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class FDClient extends AbstractClient {
    static String BASE_URL = "https://api.dictionaryapi.dev/api/v2";
    static String ENDPOINT = "/entries";
    static String LANG_CODE = "/en/";
    RestTemplate restTemplate;


    public ResponseEntity<List<FDEntry>> request(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return restTemplate.exchange(
                BASE_URL + ENDPOINT + LANG_CODE + word,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }
}

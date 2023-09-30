package com.linguarium.client.urban;

import com.linguarium.client.AbstractClient;
import com.linguarium.client.Client;
import com.linguarium.client.urban.dto.UrbanResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Client
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Slf4j
public class UrbanClient extends AbstractClient {
    static final String URBAN_API_AUTH_HEADER_HOST = "X-RapidAPI-Host";
    static final String URBAN_API_AUTH_HEADER_HOST_VALUE = "mashape-community-urban-dictionary.p.rapidapi.com";
    static final String URBAN_API_AUTH_HEADER_KEY = "X-RapidAPI-Key";
    static final String BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com";
    static final String ENDPOINT = "/define";

    @Value("${external.api.key.urban}")
    String URBAN_API_AUTH_HEADER_KEY_VALUE;

    RestTemplate restTemplate;


    public ResponseEntity<UrbanResponse> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(URBAN_API_AUTH_HEADER_HOST, URBAN_API_AUTH_HEADER_HOST_VALUE);
        headers.set(URBAN_API_AUTH_HEADER_KEY, URBAN_API_AUTH_HEADER_KEY_VALUE);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(BASE_URL + ENDPOINT)
                .queryParam("term", "{term}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("term", word);


        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UrbanResponse.class, params);
    }
}

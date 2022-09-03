package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.external_api.response.urban.UrbanResponse;
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
public class UrbanClient {
    static String URBAN_API_AUTH_HEADER_HOST = "X-RapidAPI-Host";
    static String URBAN_API_AUTH_HEADER_HOST_VALUE = "mashape-community-urban-dictionary.p.rapidapi.com";
    static String URBAN_API_AUTH_HEADER_KEY = "X-RapidAPI-Key";
    static String URBAN_API_AUTH_HEADER_KEY_VALUE = "7f58826f86mshccb070bd9f1dde8p1a6b3fjsnd5a086d2813c";
    static String BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com";
    static String ENDPOINT = "/define";
    RestTemplate restTemplate;


    public ResponseEntity<UrbanResponse> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(URBAN_API_AUTH_HEADER_HOST, URBAN_API_AUTH_HEADER_HOST_VALUE);
        headers.set(URBAN_API_AUTH_HEADER_KEY, URBAN_API_AUTH_HEADER_KEY_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

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

package com.almonium.analyzer.client.urban;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.AbstractClient;
import com.almonium.analyzer.client.Client;
import com.almonium.analyzer.client.urban.dto.UrbanResponse;
import com.almonium.config.properties.ExternalApiProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Client
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UrbanClient extends AbstractClient {
    private static final String URBAN_API_AUTH_HEADER_HOST = "X-RapidAPI-Host";
    private static final String URBAN_API_AUTH_HEADER_HOST_VALUE = "mashape-community-urban-dictionary.p.rapidapi.com";
    private static final String URBAN_API_AUTH_HEADER_KEY = "X-RapidAPI-Key";
    private static final String BASE_URL = "https://mashape-community-urban-dictionary.p.rapidapi.com";
    private static final String ENDPOINT = "/define";

    ExternalApiProperties externalApiProperties;
    RestTemplate restTemplate;

    public ResponseEntity<UrbanResponse> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(URBAN_API_AUTH_HEADER_HOST, URBAN_API_AUTH_HEADER_HOST_VALUE);
        headers.set(URBAN_API_AUTH_HEADER_KEY, externalApiProperties.getKey().getUrban());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(BASE_URL + ENDPOINT)
                .queryParam("term", "{term}")
                .encode()
                .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("term", word);

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), UrbanResponse.class, params);
    }
}

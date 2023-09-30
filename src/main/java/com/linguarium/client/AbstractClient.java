package com.linguarium.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.linguarium.util.GeneralUtils.queryBuilder;

public abstract class AbstractClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected <T> ResponseEntity<List<T>> requestList(String URL, Map<String, String> params, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(URL, params.keySet());

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                },
                params);

        List<T> resultList = new ArrayList<>();
        for (Map<String, Object> item : response.getBody()) {
            T resultItem = objectMapper.convertValue(item, clazz);
            resultList.add(resultItem);
        }

        return ResponseEntity.status(response.getStatusCode()).headers(response.getHeaders()).body(resultList);
    }

    protected <T> ResponseEntity<T> request(String URL, Map<String, String> params, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(URL, params.keySet());

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                new HttpEntity<>(headers),

                clazz,
                params);
    }
}

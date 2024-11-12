package com.almonium.analyzer.client;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.GeneralUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public abstract class AbstractClient {
    RestTemplate restTemplate = new RestTemplate();
    ObjectMapper objectMapper = new ObjectMapper();

    protected <T> ResponseEntity<List<T>> requestList(String url, Map<String, String> params, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = GeneralUtils.queryBuilder(url, params.keySet());

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {}, params);

        if (response.getBody() == null) {
            throw new RuntimeException("Body of response is null!");
        }

        List<T> resultList = new ArrayList<>();
        for (Map<String, Object> item : response.getBody()) {
            T resultItem = objectMapper.convertValue(item, clazz);
            resultList.add(resultItem);
        }

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(resultList);
    }

    protected <T> ResponseEntity<T> request(String url, Map<String, String> params, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = GeneralUtils.queryBuilder(url, params.keySet());

        return restTemplate.exchange(urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), clazz, params);
    }
}

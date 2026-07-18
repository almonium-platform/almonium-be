package com.almonium.analyzer.client;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.util.GeneralUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExternalApiHttpClient {
    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    public <T> ResponseEntity<List<T>> getList(String url, Map<String, String> params, Class<T> responseType) {
        HttpHeaders headers = jsonHeaders();
        String urlTemplate = GeneralUtils.queryBuilder(url, params.keySet());

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {}, params);

        if (response.getBody() == null) {
            throw new IllegalStateException("Body of response is null");
        }

        List<T> result = new ArrayList<>();
        for (Map<String, Object> item : response.getBody()) {
            result.add(objectMapper.convertValue(item, responseType));
        }

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(result);
    }

    public <T> ResponseEntity<T> get(String url, Map<String, String> params, Class<T> responseType) {
        String urlTemplate = GeneralUtils.queryBuilder(url, params.keySet());
        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(jsonHeaders()), responseType, params);
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}

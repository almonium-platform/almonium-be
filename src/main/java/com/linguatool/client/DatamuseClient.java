package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.api.response.datamuse.DatamuseEntryDto;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class DatamuseClient {
    static String BASE_URL = "https://api.datamuse.com";
    static String ENDPOINT = "/words";
    RestTemplate restTemplate;


    public ResponseEntity<List<DatamuseEntryDto>> request(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = UriComponentsBuilder.fromHttpUrl(BASE_URL + ENDPOINT)
            .queryParam("sl", "{sl}")
            .encode()
            .toUriString();

        Map<String, String> params = new HashMap<>();
        params.put("sl", word);

        return restTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            }, params);
    }

}

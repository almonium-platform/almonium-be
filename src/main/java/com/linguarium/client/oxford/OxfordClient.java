package com.linguarium.client.oxford;

import com.linguarium.client.AbstractClient;
import com.linguarium.client.Client;
import com.linguarium.client.oxford.dto.OxfordResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@Slf4j
public class OxfordClient extends AbstractClient {
    static String API_ID_HEADER_NAME = "app_id";
    static String API_ID_HEADER_VALUE = "22117aac";

    static String APIKEY_HEADER_NAME = "app_key";

    static String BASE_URL = "https://od-api.oxforddictionaries.com/api/v2";
    static String ENDPOINT = "/define";

    static String ENTRIES = "/entries";
    static String SENTENCES = "/sentences";
    static String TRANSLATIONS = "/translations";
    static String LEMMAS = "/lemmas";
    static String LANG_CODE = "/en-us";

    @Value("${external.api.key.oxford}")
    String APIKEY_HEADER_VALUE;

    RestTemplate restTemplate;

    public ResponseEntity<OxfordResponse> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(APIKEY_HEADER_NAME, APIKEY_HEADER_VALUE);

        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);


        return restTemplate.exchange(
                BASE_URL + ENTRIES + LANG_CODE + '/' + word,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OxfordResponse.class);
    }
}

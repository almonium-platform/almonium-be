package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.api.response.oxford.OxfordResponse;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class OxfordClient {
    static String API_ID_HEADER_NAME = "app_id";
    static String API_ID_HEADER_VALUE = "22117aac";

    static String APIKEY_HEADER_NAME = "app_key";
    static String APIKEY_HEADER_VALUE = "19e23b66eb25ac7276517521e65ecfc5";

    static String BASE_URL = "https://od-api.oxforddictionaries.com/api/v2";
    static String ENDPOINT = "/define";

    static String ENTRIES = "/entries";
    static String SENTENCES = "/sentences";
    static String TRANSLATIONS = "/translations";
    static String LEMMAS = "/lemmas";
    static String LANG_CODE = "/en-us";

    RestTemplate restTemplate;


    public ResponseEntity<OxfordResponse> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(APIKEY_HEADER_NAME, APIKEY_HEADER_VALUE);

        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);


        return restTemplate.exchange(
            BASE_URL + ENTRIES + LANG_CODE + '/' + word,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            OxfordResponse.class);
    }

}

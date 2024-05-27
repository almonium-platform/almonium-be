package linguarium.engine.client.oxford;

import static lombok.AccessLevel.PRIVATE;

import linguarium.engine.client.AbstractClient;
import linguarium.engine.client.Client;
import linguarium.engine.client.oxford.dto.OxfordResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
    String apikeyHeaderValue;

    RestTemplate restTemplate;

    public ResponseEntity<OxfordResponse> submit(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(APIKEY_HEADER_NAME, apikeyHeaderValue);

        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return restTemplate.exchange(
                BASE_URL + ENTRIES + LANG_CODE + '/' + word,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OxfordResponse.class);
    }
}

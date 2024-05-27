package linguarium.engine.client.freedictionary;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import linguarium.engine.client.AbstractClient;
import linguarium.engine.client.Client;
import linguarium.engine.client.freedictionary.dto.FDEntry;
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

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class FDClient extends AbstractClient {
    static String BASE_URL = "https://api.dictionaryapi.dev/api/v2";
    static String ENDPOINT = "/entries";
    static String LANG_CODE = "/en/";
    RestTemplate restTemplate;

    public ResponseEntity<List<FDEntry>> request(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return restTemplate.exchange(
                BASE_URL + ENDPOINT + LANG_CODE + word,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {});
    }
}

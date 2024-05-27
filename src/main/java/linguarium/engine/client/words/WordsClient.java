package linguarium.engine.client.words;

import static linguarium.util.GeneralUtils.queryBuilder;
import static lombok.AccessLevel.PRIVATE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import linguarium.engine.client.AbstractClient;
import linguarium.engine.client.Client;
import linguarium.engine.client.words.dto.WordsReportDto;
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
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class WordsClient extends AbstractClient {
    private static final String API_ID_HEADER_NAME = "X-RapidAPI-Host";
    private static final String API_ID_HEADER_VALUE = "wordsapiv1.p.rapidapi.com";
    private static final String API_KEY_HEADER_NAME = "X-RapidAPI-Key";
    private static final String BASE_URL = "https://wordsapiv1.p.rapidapi.com/words/";
    private static final String RANDOM = "random";

    RestTemplate restTemplate;
    String apiKeyHeaderValue;

    public WordsClient(RestTemplate restTemplate, @Value("${external.api.key.words}") String apiKeyHeaderValue) {
        this.restTemplate = restTemplate;
        this.apiKeyHeaderValue = apiKeyHeaderValue;
    }

    public ResponseEntity<WordsReportDto> getReport(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, apiKeyHeaderValue);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL + word, List.of());

        Map<String, Object> params = new HashMap<>();

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), WordsReportDto.class, params);
    }

    public ResponseEntity<WordsReportDto> getRandomWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(API_ID_HEADER_NAME, API_ID_HEADER_VALUE);
        headers.set(API_KEY_HEADER_NAME, apiKeyHeaderValue);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL, List.of(RANDOM));

        Map<String, Object> params = new HashMap<>();
        params.put(RANDOM, true);

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), WordsReportDto.class, params);
    }
}

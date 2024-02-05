package com.linguarium.client.wordnik;

import static com.linguarium.util.GeneralUtils.queryBuilder;
import static lombok.AccessLevel.PRIVATE;

import com.linguarium.client.AbstractClient;
import com.linguarium.client.Client;
import com.linguarium.client.wordnik.dto.WordnikAudioDto;
import com.linguarium.client.wordnik.dto.WordnikRandomWordDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Client
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@Slf4j
public class WordnikClient extends AbstractClient {
    static int START_YEAR_VALUE = 1950;
    static int END_YEAR_VALUE = 2022;
    static final String APIKEY_HEADER_NAME = "api_key";
    static final String BASE_URL = "https://api.wordnik.com/v4/word.json/";
    static final String BASE_URL_WORDS = "https://api.wordnik.com/v4/words.json/randomWord";

    static final String EXAMPLES = "/examples";
    static final String DEFINITIONS = "/definitions";
    static final String HYPERNATION = "/hypernation";
    static final String TOP_EXAMPLE = "/topExample";
    static final String RELATED_WORDS = "/relatedWords";
    static final String PRONUNCIATION = "/pronunciation";
    static final String PHRASES = "/phrases";
    static final String AUDIO = "/audio";
    static final String RANDOM_WORD = "";
    static final String USE_CANONICAL = "useCanonical";
    static final String END_YEAR = "endYear";
    static final String START_YEAR = "startYear";
    static final String FREQUENCY = "/frequency";
    static final String MIN_LENGTH = "minLength";
    static final String INCLUDE_POS = "includePartOfSpeech";
    static final String HAS_DICT_DEF = "hasDictionaryDef";
    static final String INCLUDE_POS_VALUE = "noun%2Cadjective%2Cadverb%2Cverb-intransitive%2Cverb-transitive";

    @Value("${external.api.key.wordnik}")
    String apikeyHeaderValue;

    RestTemplate restTemplate;

    public ResponseEntity<WordnikRandomWordDto> getRandomWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, apikeyHeaderValue);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> params = new HashMap<>();
        params.put(HAS_DICT_DEF, true);
        params.put(INCLUDE_POS, INCLUDE_POS_VALUE);
        params.put(MIN_LENGTH, 5);

        String urlTemplate = queryBuilder(BASE_URL_WORDS, List.of(HAS_DICT_DEF, INCLUDE_POS, MIN_LENGTH));

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), WordnikRandomWordDto.class, params);
    }

    public ResponseEntity<List<WordnikAudioDto>> getAudioFile(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, apikeyHeaderValue);
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL + word + AUDIO, List.of());

        Map<String, Object> params = new HashMap<>();

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {}, params);
    }
}

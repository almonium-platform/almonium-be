package com.almonium.analyzer.client.wordnik;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.client.AbstractClient;
import com.almonium.analyzer.client.Client;
import com.almonium.analyzer.client.wordnik.dto.WordnikAudioDto;
import com.almonium.analyzer.client.wordnik.dto.WordnikRandomWordDto;
import com.almonium.config.properties.ExternalApiProperties;
import com.almonium.util.GeneralUtils;
import java.util.HashMap;
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
import org.springframework.web.client.RestTemplate;

@Client
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class WordnikClient extends AbstractClient {
    private static final int START_YEAR_VALUE = 1950;
    private static final int END_YEAR_VALUE = 2022;
    private static final String APIKEY_HEADER_NAME = "api_key";
    private static final String BASE_URL = "https://api.wordnik.com/v4/word.json/";
    private static final String BASE_URL_WORDS = "https://api.wordnik.com/v4/words.json/randomWord";

    private static final String EXAMPLES = "/examples";
    private static final String DEFINITIONS = "/definitions";
    private static final String HYPERNATION = "/hypernation";
    private static final String TOP_EXAMPLE = "/topExample";
    private static final String RELATED_WORDS = "/relatedWords";
    private static final String PRONUNCIATION = "/pronunciation";
    private static final String PHRASES = "/phrases";
    private static final String AUDIO = "/audio";
    private static final String RANDOM_WORD = "";
    private static final String USE_CANONICAL = "useCanonical";
    private static final String END_YEAR = "endYear";
    private static final String START_YEAR = "startYear";
    private static final String FREQUENCY = "/frequency";
    private static final String MIN_LENGTH = "minLength";
    private static final String INCLUDE_POS = "includePartOfSpeech";
    private static final String HAS_DICT_DEF = "hasDictionaryDef";
    private static final String INCLUDE_POS_VALUE = "noun%2Cadjective%2Cadverb%2Cverb-intransitive%2Cverb-transitive";

    ExternalApiProperties externalApiProperties;
    RestTemplate restTemplate;

    public ResponseEntity<WordnikRandomWordDto> getRandomWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, externalApiProperties.getKey().getWordnik());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> params = new HashMap<>();
        params.put(HAS_DICT_DEF, true);
        params.put(INCLUDE_POS, INCLUDE_POS_VALUE);
        params.put(MIN_LENGTH, 5);

        String urlTemplate = GeneralUtils.queryBuilder(BASE_URL_WORDS, List.of(HAS_DICT_DEF, INCLUDE_POS, MIN_LENGTH));

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), WordnikRandomWordDto.class, params);
    }

    public ResponseEntity<List<WordnikAudioDto>> getAudioFile(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, externalApiProperties.getKey().getWordnik());
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = GeneralUtils.queryBuilder(BASE_URL + word + AUDIO, List.of());

        Map<String, Object> params = new HashMap<>();

        return restTemplate.exchange(
                urlTemplate, HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<>() {}, params);
    }
}

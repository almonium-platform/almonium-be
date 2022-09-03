package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.external_api.response.wordnik.WordnikAudioDto;
import com.linguatool.model.dto.external_api.response.wordnik.WordnikRandomWordDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linguatool.util.GeneralUtils.queryBuilder;
import static lombok.AccessLevel.PRIVATE;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class WordnikClient {
    static int START_YEAR_VALUE = 1950;
    static int END_YEAR_VALUE = 2022;
    static String APIKEY_HEADER_NAME = "api_key";
    static String APIKEY_HEADER_VALUE = "d6fsnd5tslptc5mnhao3qbdda6g9j2rbfw6yopzkr2dsq0gjo";
    static String BASE_URL = "https://api.wordnik.com/v4/word.json/";
    static String BASE_URL_WORDS = "https://api.wordnik.com/v4/words.json/randomWord";

    static String EXAMPLES = "/examples";
    static String DEFINITIONS = "/definitions";
    static String HYPERNATION = "/hypernation";
    static String TOP_EXAMPLE = "/topExample";
    static String RELATED_WORDS = "/relatedWords";
    static String PRONUNCIATION = "/pronunciation";
    static String PHRASES = "/phrases";
    static String AUDIO = "/audio";
    static String RANDOM_WORD = "";
    static String USE_CANONICAL = "useCanonical";
    static String END_YEAR = "endYear";
    static String START_YEAR = "startYear";
    static String FREQUENCY = "/frequency";
    static String MIN_LENGTH = "minLength";
    static String INCLUDE_POS = "includePartOfSpeech";
    static String HAS_DICT_DEF = "hasDictionaryDef";
    static String INCLUDE_POS_VALUE = "noun%2Cadjective%2Cadverb%2Cverb-intransitive%2Cverb-transitive";


    RestTemplate restTemplate;

    public ResponseEntity<WordnikRandomWordDto> getRandomWord() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, APIKEY_HEADER_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL_WORDS,
            List.of(HAS_DICT_DEF, INCLUDE_POS, MIN_LENGTH)
        );


        Map<String, Object> params = new HashMap<>();
        params.put(HAS_DICT_DEF, true);
        params.put(INCLUDE_POS, INCLUDE_POS_VALUE);
        params.put(MIN_LENGTH, 5);

        return restTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            WordnikRandomWordDto.class, params);
    }

    public ResponseEntity<List<WordnikAudioDto>> getAudioFile(String word) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY_HEADER_NAME, APIKEY_HEADER_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(BASE_URL + word + AUDIO,
            List.of()
        );

        Map<String, Object> params = new HashMap<>();

        return restTemplate.exchange(
            urlTemplate,
            HttpMethod.GET,
            new HttpEntity<>(headers),
            new ParameterizedTypeReference<>() {
            } , params);
    }
}

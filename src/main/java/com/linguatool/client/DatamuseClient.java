package com.linguatool.client;

import com.linguatool.annotation.Client;
import com.linguatool.model.dto.external_api.response.datamuse.DatamuseEntryDto;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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
public class DatamuseClient {
    static String BASE_URL = "https://api.datamuse.com";
    static String ENDPOINT = "/words";
    static String HOMOPHONES = "rel_hom";
    static String MEANS_LIKE = "ml";
    static String FREQUENCY = "f";
    static String ADJ_FOR_N = "rel_jjb";
    static String N_FOR_ADJ = "rel_jja";
    static String ANTONYMS = "rel_ant";
    static String SYNONYMS = "rel_syn";
    static String PREDECESSORS = "rel_bgb";
    static String FOLLOWERS = "rel_bga";
    static String SPELLED_LIKE = "sp";
    static String QUERY_ECHO = "qe";
    static String MAX = "max";
    static String PART_OF_SPEECH = "p";
    static String PRONUNCIATION = "r";
    static String DEFINITION = "d";
    static String METADATA = "md";
    static String IPA = "ipa";

    RestTemplate restTemplate;

    public ResponseEntity<List<DatamuseEntryDto>> getHomophones(String word) {
        return request(word, HOMOPHONES);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getPredecessors(String word) {
        return request(word, PREDECESSORS);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getFollowers(String word) {
        return request(word, FOLLOWERS);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getSynonyms(String word) {
        return request(word, SYNONYMS);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getAntonyms(String word) {
        return request(word, ANTONYMS);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getMeansLike(String word) {
        return request(word, MEANS_LIKE);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getNounsForAdjective(String adjective) {
        return request(adjective, N_FOR_ADJ);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getAdjectivesForNoun(String noun) {
        return request(noun, ADJ_FOR_N);
    }

    public ResponseEntity<List<DatamuseEntryDto>> getWordReport(String entry) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(
                BASE_URL + ENDPOINT,
                List.of(SPELLED_LIKE, QUERY_ECHO, METADATA, IPA, MAX)
        );

        Map<String, String> params = new HashMap<>();
        params.put(SPELLED_LIKE, entry);
        params.put(QUERY_ECHO, SPELLED_LIKE);
        params.put(METADATA, FREQUENCY + PART_OF_SPEECH + PRONUNCIATION + DEFINITION);
        params.put(MAX, String.valueOf(1));
        params.put(IPA, String.valueOf(1));

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }, params);
    }

    private ResponseEntity<List<DatamuseEntryDto>> request(String word, String parameter) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        String urlTemplate = queryBuilder(
                BASE_URL + ENDPOINT,
                List.of(parameter)
        );

        Map<String, String> params = new HashMap<>();
        params.put(parameter, word);

        return restTemplate.exchange(
                urlTemplate,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                }, params);
    }
}

package com.linguatool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguatool.client.DatamuseClient;
import com.linguatool.client.FDClient;
import com.linguatool.client.OxfordClient;
import com.linguatool.client.UrbanClient;
import com.linguatool.client.WordnikClient;
import com.linguatool.client.WordsClient;
import com.linguatool.model.dto.api.request.AnalysisDto;
import com.linguatool.model.dto.api.response.datamuse.DatamuseEntryDto;
import com.linguatool.model.dto.api.response.wordnik.WordnikAudioDto;
import com.linguatool.model.dto.api.response.words.WordsReportDto;
import com.linguatool.model.dto.lang.POS;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ExternalService {
    UrbanClient urbanClient;
    DatamuseClient datamuseClient;
    WordnikClient wordnikClient;
    WordsClient wordsClient;
    OxfordClient oxfordClient;

    ObjectMapper objectMapper;
    FDClient fdClient;
    CoreNLPService coreNLPService;

    public void create(String word) {
//        ResponseEntity<UrbanResponse> response = urbanClient.submit(word);
//        ResponseEntity<WordnikFrequencyDto> response = wordnikClient.submit(word);
//        ResponseEntity response = oxfordClient.submit(word);
//        ResponseEntity<List<FDEntry>> response = fdClient.request(word);
//        System.out.println("fd" + response);
//        ResponseEntity<List<DatamuseEntryDto>> response = datamuseClient.request(word);
//        UrbanResponse dto = response.getBody();
//        System.out.println((dto.getList().size()));

    }

    public String[] homophones() {
//        ResponseEntity<List<DatamuseEntryDto>> response = datamuseClient.request(entry);
        return null;
    }

    public List<String> getAudioLink(String word) {
        return Objects.requireNonNull(wordnikClient.getAudioFile(word).getBody())
            .stream().map(WordnikAudioDto::getFileUrl).collect(Collectors.toList());
    }

    public double getFrequency(String entry) {
        ResponseEntity<List<DatamuseEntryDto>> response = datamuseClient.getWordReport(entry);
        String fTag = response.getBody().get(0).getTags()[2];
        return Double.parseDouble(fTag.split(":")[1]);
    }

    public String[] getNounsForAdjective(String entry) {
        return Objects.requireNonNull(datamuseClient.getNounsForAdjective(entry)
            .getBody()).stream().map(DatamuseEntryDto::getWord).map(String::new).toArray(String[]::new);
    }

    public String[] getHomophones(String entry) {
        return Objects.requireNonNull(datamuseClient.getHomophones(entry)
            .getBody()).stream().map(DatamuseEntryDto::getWord).map(String::new).toArray(String[]::new);
    }

    public String[] getAdjectivesForNoun(String entry) {
        return Objects.requireNonNull(datamuseClient.getNounsForAdjective(entry)
            .getBody()).stream().map(DatamuseEntryDto::getWord).map(String::new).toArray(String[]::new);
    }

    public void singleWordAnalysis(AnalysisDto dto, String entry, List<POS> posTags, List<String> lemmas) {

        if (posTags.get(0).isAdjective()) {
            dto.setNouns(getNounsForAdjective(entry));
        } else if (posTags.get(0).equals(POS.NOUN)) {
            dto.setNouns(getAdjectivesForNoun(entry));
        }
        dto.setHomophones(getHomophones(entry));
//        dto.setFamily(getWordFamily(entry));
    }

    public String[] getWordFamily(String entry) {
        throw new NotImplementedException("not yet");
    }

    public String[] getSyllables(WordsReportDto dto) {
//        wordsClient.getReport()
        return dto.getSyllables().getList();
    }

    public String getBaseAdjectiveForComparative(String adj) {
        throw new NotImplementedException("not yet");
    }

    public String getBaseAdjectiveForSuperlative(String adj) {
        throw new NotImplementedException("not yet");
    }

    public void getReport(String entry) {
        double freq = getFrequency(entry);
        AnalysisDto analysisDto = new AnalysisDto();
        List<String> lemmas = coreNLPService.lemmatize(entry);
        analysisDto.setLemmas(lemmas.stream().map(String::new).toArray(String[]::new));

        List<POS> posTags = coreNLPService.posTagging(entry);
        analysisDto.setPosTags(posTags.stream().map(POS::toString).toArray(String[]::new));
        if (lemmas.size() != posTags.size()) {
            log.error("Lemmas don't correspond with POS tags");
        }
        if (lemmas.size() == 1) {
            if (posTags.get(0).equals(POS.ADJECTIVE_COMPARATIVE)) {
                getBaseAdjectiveForComparative(entry);
            } else if (posTags.get(0).equals(POS.ADJECTIVE_SUPERLATIVE)) {
                getBaseAdjectiveForSuperlative(entry);
            } else if (posTags.get(0).equals(POS.PROPER_NOUN_SINGULAR) || posTags.get(0).equals(POS.PROPER_NOUN_PLURAL)) {
                analysisDto.setIsProper(true);
            } else if (posTags.get(0).equals(POS.FOREIGN_WORD)) {
                analysisDto.setIsForeignWord(true);
            } else if (posTags.get(0).equals(POS.NOUN_PLURAL)) {
                analysisDto.setIsPlural(true);
            }
            singleWordAnalysis(analysisDto, entry, posTags, lemmas);
        } else if (lemmas.size() == 2) {
            if (posTags.get(0).equals(POS.TO) && posTags.get(1).equals(POS.VERB)) {
                entry = lemmas.get(1);
                singleWordAnalysis(analysisDto, entry, posTags, lemmas);
            }
        }
    }

    public WordsReportDto getRandom() {
        return wordsClient.getRandomWord().getBody();
    }
}

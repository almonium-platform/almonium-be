package com.linguatool.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linguatool.client.*;
import com.linguatool.model.dto.api.request.AnalysisDto;
import com.linguatool.model.dto.api.response.datamuse.DatamuseEntryDto;
import com.linguatool.model.dto.api.response.wordnik.WordnikAudioDto;
import com.linguatool.model.dto.api.response.words.WordsReportDto;
import com.linguatool.model.dto.api.response.yandex.YandexDto;
import com.linguatool.model.dto.lang.POS;
import com.linguatool.model.dto.lang.translation.TranslationCardDto;
import com.linguatool.model.entity.user.Language;
import com.linguatool.model.mapping.DictionaryDtoMapper;
import com.linguatool.repository.LangPairTranslatorRepository;
import com.linguatool.repository.LanguageRepository;
import com.linguatool.repository.TranslatorRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
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
    YandexClient yandexClient;
    WordsClient wordsClient;

    OxfordClient oxfordClient;

    ObjectMapper objectMapper;

    FDClient fdClient;
    CoreNLPService coreNLPService;
    LangPairTranslatorRepository langPairTranslatorRepository;
    LanguageRepository languageRepository;
    TranslatorRepository translatorRepository;
    DictionaryDtoMapper dictionaryDtoMapper;

    public ResponseEntity<?> limitExceeded(String provider) {
        return ResponseEntity.status(403).body(provider);
    }

    @SneakyThrows
    public ResponseEntity<?> translate(String langFrom, String langTo, String entry) {
        Language sourceLang = Language.fromString(langFrom);
        Language destinationLang = Language.fromString(langTo);

        if (sourceLang == null || destinationLang == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Long> translatorsIds = langPairTranslatorRepository.getByLangFromAndLangTo(
                languageRepository.findByCode(sourceLang).orElseThrow().getId(),
                languageRepository.findByCode(destinationLang).orElseThrow().getId());

        if (translatorsIds.size() == 0) {
            return ResponseEntity.notFound().build();
        } else if (translatorsIds.size() == 1) {
            if (translatorsIds.get(0).equals(translatorRepository.getYandex().getId())) {
                ResponseEntity<YandexDto> responseEntity = yandexClient.translate(entry, sourceLang, destinationLang);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    TranslationCardDto card = dictionaryDtoMapper.yandexToGeneral(responseEntity.getBody());
                    card.setProvider(translatorRepository.getYandex().getName());
                    return ResponseEntity.ok(card);
                }
                if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
                    return limitExceeded(translatorRepository.getYandex().getName());
                } else if (responseEntity.getStatusCode() == HttpStatus.NOT_IMPLEMENTED) {
                    log.error("Language pair not supported: probably, langPairTranslator table is out of date");
                    throw new Exception("Unexpectedly not supported lang pair in this provider");
                } else if (responseEntity.getStatusCode().is4xxClientError()) {
                    log.error(responseEntity.getBody().toString());
                    return ResponseEntity.internalServerError().body(responseEntity.getBody());
                } else {
                    return null;
                }

            } else {
                log.error("Cannot recognize translator");
                return ResponseEntity.internalServerError().build();
            }
        } else {
            // TODO if multiple => which engines?
            return null;
        }
    }

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

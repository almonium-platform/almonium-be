package com.almonium.analyzer.analyzer.service;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.dto.response.AnalysisDto;
import com.almonium.analyzer.analyzer.mapper.DictionaryDtoMapper;
import com.almonium.analyzer.analyzer.model.enums.POS;
import com.almonium.analyzer.client.datamuse.DatamuseClient;
import com.almonium.analyzer.client.datamuse.dto.DatamuseEntryDto;
import com.almonium.analyzer.client.google.GoogleClient;
import com.almonium.analyzer.client.google.dto.GoogleDto;
import com.almonium.analyzer.client.wordnik.WordnikClient;
import com.almonium.analyzer.client.wordnik.dto.WordnikAudioDto;
import com.almonium.analyzer.client.words.WordsClient;
import com.almonium.analyzer.client.words.dto.WordsReportDto;
import com.almonium.analyzer.client.yandex.YandexClient;
import com.almonium.analyzer.client.yandex.dto.YandexDto;
import com.almonium.analyzer.translator.dto.MLTranslationCard;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.repository.LangPairTranslatorRepository;
import com.almonium.analyzer.translator.repository.TranslatorRepository;
import com.almonium.analyzer.translator.service.TranslationService;
import com.almonium.card.core.service.LearnerFinder;
import com.almonium.user.core.model.entity.Learner;
import com.almonium.user.core.model.entity.User;
import com.google.protobuf.ByteString;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LanguageProcessor {
    private static final double LOW_BOUND = 1e-9;
    private static final double OFFSET = 10;
    private static final double SCALE = 1.153315895823627;

    CoreNLPService coreNLPService = null;
    TranslationService googleService;
    LearnerFinder learnerFinder;

    DatamuseClient datamuseClient;
    GoogleClient googleClient;
    WordnikClient wordnikClient;
    YandexClient yandexClient;
    WordsClient wordsClient;

    LangPairTranslatorRepository langPairTranslatorRepository;
    TranslatorRepository translatorRepository;

    DictionaryDtoMapper dictionaryDtoMapper;

    public MLTranslationCard bulkTranslate(String text, Language targetLang) {
        // todo deepL
        return new MLTranslationCard(
                translatorRepository.getGoogle().getName(), googleService.bulkTranslateText(text, targetLang.name()));
    }

    @SneakyThrows
    public TranslationCardDto translate(String entry, Language sourceLang, Language targetLang) {

        if (sourceLang == null || targetLang == null) {
            return null;
        }

        List<Long> translatorsIds =
                langPairTranslatorRepository.getBySourceLangAndTargetLang(sourceLang.name(), targetLang.name());

        if (translatorsIds.size() == 0) {
            return null;
        } else if (translatorsIds.size() == 1) {
            if (translatorsIds.get(0).equals(translatorRepository.getYandex().getId())) {
                ResponseEntity<YandexDto> responseEntity = yandexClient.translate(entry, sourceLang, targetLang);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    TranslationCardDto card = dictionaryDtoMapper.yandexToGeneral(responseEntity.getBody());
                    card.setProvider(translatorRepository.getYandex().getName());
                    return card;
                }
                if (responseEntity.getStatusCode() == HttpStatus.FORBIDDEN) {
                    log.error("LIMIT EXCEEDED");
                    return null;
                } else if (responseEntity.getStatusCode() == HttpStatus.NOT_IMPLEMENTED) {
                    log.error("Language pair not supported: probably, langPairTranslator table is out of date");
                    throw new Exception("Unexpectedly not supported lang pair in this provider");
                } else if (responseEntity.getStatusCode().is4xxClientError()) {
                    log.error(responseEntity.getBody().toString());
                    return null;
                } else {
                    return null;
                }

            } else {
                log.error("Cannot recognize translator");
                return null;
            }
        } else {
            // TODO if multiple => which engines?
            return null;
        }
    }

    public List<String> getAudioLink(String word) {
        return Objects.requireNonNull(wordnikClient.getAudioFile(word).getBody()).stream()
                .map(WordnikAudioDto::getFileUrl)
                .collect(Collectors.toList());
    }

    @Deprecated
    public double getFrequencyDatamuse(String entry) {
        ResponseEntity<List<DatamuseEntryDto>> response = datamuseClient.getWordReport(entry);
        String fTag = Arrays.stream(response.getBody().get(0).getTags())
                .filter(tag -> tag.startsWith("f"))
                .findFirst()
                .get();
        return Double.parseDouble(fTag.split(":")[1]);
    }

    public Double getFrequency(String entry, Language language) {
        List<GoogleDto> list = googleClient.get(entry, language).getBody();
        return (list.size() > 1) ? Double.parseDouble(list.get(0).getTimeseries()[0]) : null;
    }

    public String[] getNounsForAdjective(String entry) {
        return Objects.requireNonNull(datamuseClient.getNounsForAdjective(entry).getBody()).stream()
                .map(DatamuseEntryDto::getWord)
                .map(String::new)
                .toArray(String[]::new);
    }

    public String[] getHomophones(String entry) {
        return Objects.requireNonNull(datamuseClient.getHomophones(entry).getBody()).stream()
                .map(DatamuseEntryDto::getWord)
                .map(String::new)
                .toArray(String[]::new);
    }

    public String[] getAdjectivesForNoun(String entry) {
        return Objects.requireNonNull(datamuseClient.getNounsForAdjective(entry).getBody()).stream()
                .map(DatamuseEntryDto::getWord)
                .map(String::new)
                .toArray(String[]::new);
    }

    private void singleWordAnalysis(
            AnalysisDto analysisDto, String entry, List<POS> posTags, List<String> lemmas, Language from, Language to) {
        if (posTags.get(0).equals(POS.ADJECTIVE_COMPARATIVE)) {
            getBaseAdjectiveForComparative(entry);
        } else if (posTags.get(0).equals(POS.ADJECTIVE_SUPERLATIVE)) {
            getBaseAdjectiveForSuperlative(entry);
        } else if (posTags.get(0).equals(POS.PROPER_NOUN_SINGULAR)
                || posTags.get(0).equals(POS.PROPER_NOUN_PLURAL)) {
            analysisDto.setIsProper(true);
        } else if (posTags.get(0).equals(POS.FOREIGN_WORD)) {
            analysisDto.setIsForeignWord(true);
        } else if (posTags.get(0).equals(POS.NOUN_PLURAL)) {
            analysisDto.setIsPlural(true);
        } else if (posTags.get(0).isAdjective()) {
            analysisDto.setNouns(getNounsForAdjective(entry));
        } else if (posTags.get(0).equals(POS.NOUN)) {
            analysisDto.setAdjectives(getAdjectivesForNoun(entry));
        }
        analysisDto.setTranslationCards(this.translate(entry, from, to));
        analysisDto.setHomophones(getHomophones(entry));
    }

    public AnalysisDto getReport(String entry, Language sourceLang, User user) {
        Learner learner = learnerFinder.findLearner(user, sourceLang);
        AnalysisDto analysisDto = new AnalysisDto();
        List<String> lemmas = coreNLPService.lemmatize(entry);
        analysisDto.setLemmas(lemmas.stream().map(String::new).toArray(String[]::new));

        Language fluentLanguage = learner.getUser().getFluentLangs().iterator().next();

        List<POS> posTags = coreNLPService.posTagging(entry);
        analysisDto.setPosTags(posTags.stream().map(POS::toString).toArray(String[]::new));
        Double freq = getFrequency(entry, sourceLang);
        if (freq != null) {
            analysisDto.setFrequency(calculateRelativeFrequency(freq));
        }

        if (lemmas.size() != posTags.size()) {
            log.error("Lemmas don't correspond with POS tags");
        }
        if (lemmas.size() == 1) {
            log.info("one lemma analysis");
            singleWordAnalysis(analysisDto, entry, posTags, lemmas, sourceLang, fluentLanguage);
        } else if (lemmas.size() == 2) {
            // TO VERB case
            if (posTags.get(0).equals(POS.TO) && posTags.get(1).equals(POS.VERB)) {
                entry = lemmas.get(1);
                singleWordAnalysis(analysisDto, entry, posTags, lemmas, sourceLang, fluentLanguage);
            }
        }
        return analysisDto;
    }

    public WordsReportDto getRandom() {
        return wordsClient.getRandomWord().getBody();
    }

    public String getBaseAdjectiveForComparative(String adj) {
        throw new NotImplementedException("not yet");
    }

    public String getBaseAdjectiveForSuperlative(String adj) {
        throw new NotImplementedException("not yet");
    }

    public ByteString textToSpeech(String code, String text) {
        return googleService.textToSpeech(code, text);
    }

    private static double calculateRelativeFrequency(double frequency) {
        if (frequency == 0) {
            return 0;
        }
        if (frequency < LOW_BOUND) {
            return 1;
        }
        return SCALE * (Math.log10(frequency) + OFFSET);
    }
}

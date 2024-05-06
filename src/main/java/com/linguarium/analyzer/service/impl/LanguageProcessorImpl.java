package com.linguarium.analyzer.service.impl;

import static lombok.AccessLevel.PRIVATE;

import com.google.protobuf.ByteString;
import com.linguarium.analyzer.dto.AnalysisDto;
import com.linguarium.analyzer.mapper.DictionaryDtoMapper;
import com.linguarium.analyzer.service.LanguageProcessor;
import com.linguarium.analyzer.model.POS;
import com.linguarium.client.datamuse.DatamuseClient;
import com.linguarium.client.datamuse.dto.DatamuseEntryDto;
import com.linguarium.client.google.GoogleClient;
import com.linguarium.client.google.dto.GoogleDto;
import com.linguarium.client.wordnik.WordnikClient;
import com.linguarium.client.wordnik.dto.WordnikAudioDto;
import com.linguarium.client.words.WordsClient;
import com.linguarium.client.words.dto.WordsReportDto;
import com.linguarium.client.yandex.YandexClient;
import com.linguarium.client.yandex.dto.YandexDto;
import com.linguarium.translator.dto.MLTranslationCard;
import com.linguarium.translator.dto.TranslationCardDto;
import com.linguarium.translator.model.Language;
import com.linguarium.translator.repository.LangPairTranslatorRepository;
import com.linguarium.translator.repository.TranslatorRepository;
import com.linguarium.translator.service.TranslationService;
import com.linguarium.user.model.Learner;
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
public class LanguageProcessorImpl implements LanguageProcessor {
    private static final double LOW_BOUND = 1e-9;
    private static final double OFFSET = 10;
    private static final double SCALE = 1.153315895823627;

    DatamuseClient datamuseClient;
    GoogleClient googleClient;
    WordnikClient wordnikClient;
    YandexClient yandexClient;
    WordsClient wordsClient;
    CoreNLPServiceImpl coreNLPServiceImpl;
    TranslationService googleService;
    LangPairTranslatorRepository langPairTranslatorRepository;
    TranslatorRepository translatorRepository;
    DictionaryDtoMapper dictionaryDtoMapper;

    @Override
    public MLTranslationCard bulkTranslate(String text, Language targetLang) {
        // todo deepL
        return new MLTranslationCard(
                translatorRepository.getGoogle().getName(), googleService.bulkTranslateText(text, targetLang.name()));
    }

    @Override
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

    @Override
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

    @Override
    public AnalysisDto getReport(String entry, String languageCode, Learner learner) {
        AnalysisDto analysisDto = new AnalysisDto();
        List<String> lemmas = coreNLPServiceImpl.lemmatize(entry);
        analysisDto.setLemmas(lemmas.stream().map(String::new).toArray(String[]::new));

        Language sourceLang = Language.valueOf(languageCode);
        Language fluentLanguage =
                Language.valueOf(learner.getFluentLangs().iterator().next());

        List<POS> posTags = coreNLPServiceImpl.posTagging(entry);
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

    @Override
    public WordsReportDto getRandom() {
        return wordsClient.getRandomWord().getBody();
    }

    public String getBaseAdjectiveForComparative(String adj) {
        throw new NotImplementedException("not yet");
    }

    public String getBaseAdjectiveForSuperlative(String adj) {
        throw new NotImplementedException("not yet");
    }

    @Override
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

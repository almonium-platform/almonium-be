package com.linguarium.engine.analyzer.service;

import com.google.protobuf.ByteString;
import com.linguarium.engine.analyzer.dto.AnalysisDto;
import com.linguarium.engine.client.words.dto.WordsReportDto;
import com.linguarium.engine.translator.dto.MLTranslationCard;
import com.linguarium.engine.translator.dto.TranslationCardDto;
import com.linguarium.engine.translator.model.Language;
import com.linguarium.user.core.model.Learner;
import java.util.List;

public interface LanguageProcessor {
    MLTranslationCard bulkTranslate(String text, Language targetLang);

    AnalysisDto getReport(String entry, String languageCode, Learner learner);

    WordsReportDto getRandom();

    TranslationCardDto translate(String entry, Language sourceLang, Language targetLang);

    ByteString textToSpeech(String code, String text);

    List<String> getAudioLink(String word);
}

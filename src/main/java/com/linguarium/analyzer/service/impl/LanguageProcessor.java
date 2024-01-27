package com.linguarium.analyzer.service.impl;

import com.google.protobuf.ByteString;
import com.linguarium.analyzer.dto.AnalysisDto;
import com.linguarium.client.words.dto.WordsReportDto;
import com.linguarium.translator.dto.MLTranslationCard;
import com.linguarium.translator.dto.TranslationCardDto;
import com.linguarium.translator.model.Language;
import com.linguarium.user.model.Learner;

import java.util.List;

public interface LanguageProcessor {
    MLTranslationCard bulkTranslate(String text, Language targetLang);

    AnalysisDto getReport(String entry, String languageCode, Learner learner);

    WordsReportDto getRandom();

    TranslationCardDto translate(String entry, Language sourceLang, Language targetLang);

    ByteString textToSpeech(String code, String text);

    List<String> getAudioLink(String word);
}

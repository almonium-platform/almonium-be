package com.almonium.analyzer.analyzer.service;

import com.almonium.analyzer.analyzer.dto.AnalysisDto;
import com.almonium.analyzer.client.words.dto.WordsReportDto;
import com.almonium.analyzer.translator.dto.MLTranslationCard;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.model.entity.Learner;
import com.google.protobuf.ByteString;
import java.util.List;

public interface LanguageProcessor {
    MLTranslationCard bulkTranslate(String text, Language targetLang);

    AnalysisDto getReport(String entry, String languageCode, Learner learner);

    WordsReportDto getRandom();

    TranslationCardDto translate(String entry, Language sourceLang, Language targetLang);

    ByteString textToSpeech(String code, String text);

    List<String> getAudioLink(String word);
}

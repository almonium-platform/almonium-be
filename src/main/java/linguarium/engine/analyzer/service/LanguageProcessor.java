package linguarium.engine.analyzer.service;

import com.google.protobuf.ByteString;
import java.util.List;
import linguarium.engine.analyzer.dto.AnalysisDto;
import linguarium.engine.client.words.dto.WordsReportDto;
import linguarium.engine.translator.dto.MLTranslationCard;
import linguarium.engine.translator.dto.TranslationCardDto;
import linguarium.engine.translator.model.Language;
import linguarium.user.core.model.Learner;

public interface LanguageProcessor {
    MLTranslationCard bulkTranslate(String text, Language targetLang);

    AnalysisDto getReport(String entry, String languageCode, Learner learner);

    WordsReportDto getRandom();

    TranslationCardDto translate(String entry, Language sourceLang, Language targetLang);

    ByteString textToSpeech(String code, String text);

    List<String> getAudioLink(String word);
}

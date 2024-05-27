package linguarium.engine.client.google;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import java.util.Map;
import linguarium.engine.client.AbstractClient;
import linguarium.engine.client.Client;
import linguarium.engine.client.google.dto.GoogleDto;
import linguarium.engine.translator.model.Language;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

@Client
@AllArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
@Slf4j
public class GoogleClient extends AbstractClient {
    static String CASE_INSENSITIVE = "case_insensitive";
    static String URL = "https://books.google.com/ngrams/json";
    static String CONTENT = "content";
    static String START_YEAR = "year_start";
    static String END_YEAR = "year_end";
    static String CORPUS = "corpus";
    static String SMOOTHING = "smoothing";
    static String AME_CORPUS = "en-US-2019";
    static String BRE_CORPUS = "en-GB-2019";
    static String VERB_SUFFIX = "_INF";
    static int SMOOTHING_VALUE = 0;
    static int START_YEAR_VALUE = 2018;
    static int END_YEAR_VALUE = 2019;
    static boolean IS_CASE_INSENSITIVE = true;
    static Map<Language, String> languageMap = Map.of(
            Language.EN, "en-2019",
            Language.DE, "de-2019",
            Language.FR, "fr-2019",
            Language.ES, "es-2019",
            Language.RU, "ru-2019");

    public ResponseEntity<List<GoogleDto>> get(String entry, Language language) {
        Map<String, String> params = Map.of(
                CONTENT, entry,
                CORPUS, languageMap.get(language),
                START_YEAR, String.valueOf(START_YEAR_VALUE),
                END_YEAR, String.valueOf(END_YEAR_VALUE),
                SMOOTHING, String.valueOf(SMOOTHING_VALUE),
                CASE_INSENSITIVE, String.valueOf(IS_CASE_INSENSITIVE));
        return super.requestList(URL, params, GoogleDto.class);
    }
}

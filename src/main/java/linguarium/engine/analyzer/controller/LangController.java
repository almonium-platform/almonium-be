package linguarium.engine.analyzer.controller;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;
import linguarium.auth.common.model.entity.Principal;
import linguarium.card.core.dto.CardDto;
import linguarium.card.core.service.CardService;
import linguarium.engine.analyzer.dto.AnalysisDto;
import linguarium.engine.analyzer.service.LanguageProcessor;
import linguarium.engine.client.words.dto.WordsReportDto;
import linguarium.engine.translator.dto.MLTranslationCard;
import linguarium.engine.translator.dto.TranslationCardDto;
import linguarium.engine.translator.model.enums.Language;
import linguarium.util.annotation.Auth;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lang")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LangController {
    CardService cardService;
    LanguageProcessor languageProcessor;

    @GetMapping("/cards/search/{text}")
    public ResponseEntity<List<CardDto>> search(@PathVariable String text, @Auth Principal auth) {
        return ResponseEntity.ok(cardService.searchByEntry(text, auth.getUser().getLearner()));
    }

    @GetMapping("/translate/{langFrom}/{langTo}/{text}")
    public ResponseEntity<TranslationCardDto> translate(
            @PathVariable String langFrom, @PathVariable String langTo, @PathVariable String text) {
        return ResponseEntity.ok(
                languageProcessor.translate(text, Language.valueOf(langFrom), Language.valueOf(langTo)));
    }

    @PostMapping("/translations/{langTo}/bulk")
    public ResponseEntity<MLTranslationCard> bulkTranslate(@PathVariable String langTo, @RequestBody String text) {
        return ResponseEntity.ok(languageProcessor.bulkTranslate(text, Language.valueOf(langTo)));
    }

    @GetMapping("/words/random")
    public ResponseEntity<WordsReportDto> random() {
        return ResponseEntity.ok(languageProcessor.getRandom());
    }

    @GetMapping("/words/{word}/audio")
    public ResponseEntity<List<String>> audio(@PathVariable String word) {
        return ResponseEntity.ok(languageProcessor.getAudioLink(word));
    }

    @GetMapping("/words/{text}/audio/{lang}")
    public ResponseEntity<Resource> bulkPronounce(@PathVariable String lang, @PathVariable String text) {
        byte[] bytes = languageProcessor.textToSpeech(lang, text).toByteArray();

        return ResponseEntity.ok()
                .headers(createAudioHeaders())
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(new ByteArrayResource(bytes));
    }

    @GetMapping("/words/{text}/{lang}/report")
    public ResponseEntity<AnalysisDto> getReport(
            @PathVariable String text, @PathVariable String lang, @Auth Principal auth) {
        return ResponseEntity.ok(
                languageProcessor.getReport(text, lang, auth.getUser().getLearner()));
    }

    private HttpHeaders createAudioHeaders() {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.mp3");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }
}

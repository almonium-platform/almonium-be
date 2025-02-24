package com.almonium.analyzer.analyzer.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.dto.response.AnalysisDto;
import com.almonium.analyzer.analyzer.service.LanguageProcessor;
import com.almonium.analyzer.client.words.dto.WordsReportDto;
import com.almonium.analyzer.translator.dto.MLTranslationCard;
import com.almonium.analyzer.translator.dto.TranslationCardDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.card.core.dto.response.CardDto;
import com.almonium.card.core.service.CardService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

@Tag(name = "Learning")
@RestController
@RequestMapping("/lang")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LangController {
    CardService cardService;
    LanguageProcessor languageProcessor;

    @GetMapping("/cards/{lang}/search/{text}")
    public ResponseEntity<List<CardDto>> search(
            @PathVariable String text, @PathVariable Language lang, @Auth User user) {
        return ResponseEntity.ok(cardService.searchByEntry(text, lang, user));
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
            @PathVariable String text, @PathVariable Language lang, @Auth User user) {
        return ResponseEntity.ok(languageProcessor.getReport(text, lang, user));
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

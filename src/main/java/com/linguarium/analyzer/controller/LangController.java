package com.linguarium.analyzer.controller;

import com.linguarium.analyzer.dto.AnalysisDto;
import com.linguarium.analyzer.service.impl.LanguageProcessorImpl;
import com.linguarium.auth.annotation.CurrentUser;
import com.linguarium.auth.model.LocalUser;
import com.linguarium.card.dto.CardDto;
import com.linguarium.card.service.CardService;
import com.linguarium.client.words.dto.WordsReportDto;
import com.linguarium.translator.model.Language;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequestMapping("/api/lang")
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LangController {
    CardService cardService;
    LanguageProcessorImpl languageProcessorImpl;

    public LangController(CardService cardService, LanguageProcessorImpl languageProcessorImpl) {
        this.cardService = cardService;
        this.languageProcessorImpl = languageProcessorImpl;
    }

    @CrossOrigin
    @GetMapping("/stack-search/{text}")
    public ResponseEntity<List<CardDto>> search(@PathVariable String text, @CurrentUser LocalUser localUser) {
        return ResponseEntity.ok(cardService.searchByEntry(text, localUser.getUser()));
    }

    @GetMapping("/translate/{langFrom}/{langTo}/{text}")
    public ResponseEntity<?> translate(@PathVariable String langFrom,
                                       @PathVariable String langTo,
                                       @PathVariable String text) {
        return ResponseEntity.ok(languageProcessorImpl.translate(text, Language.fromString(langFrom), Language.fromString(langTo)));
    }

    @PostMapping("/bulk-translate/{langTo}")
    public ResponseEntity<?> bulkTranslate(@PathVariable String langTo,
                                           @RequestBody String text) {
        return ResponseEntity.ok(languageProcessorImpl.bulkTranslate(text, Language.fromString(langTo)));
    }

    @GetMapping("/random")
    public ResponseEntity<WordsReportDto> random() {
        return ResponseEntity.ok(languageProcessorImpl.getRandom());
    }

    @GetMapping("/audio/{word}")
    public ResponseEntity<?> audio(@PathVariable String word) {
        return ResponseEntity.ok(languageProcessorImpl.getAudioLink(word));
    }

    @GetMapping("/audio/{lang}/{text}/file.mp3")
    public ResponseEntity<Resource> bulkPronounce(@PathVariable String lang, @PathVariable String text) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.mp3");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] bytes = languageProcessorImpl.textToSpeech(lang, text).toByteArray();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }

    @GetMapping("/file.mp3")
    public ResponseEntity<AnalysisDto> getReporddt() {
        return null;
    }

    @RequestMapping(path = "/download/file.mp3", method = RequestMethod.GET)
    public ResponseEntity<Resource> download() throws IOException {

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=file.mp3");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        byte[] bytes = languageProcessorImpl.textToSpeech("en", "How come you didn't wake up early?").toByteArray();
        ByteArrayResource resource = new ByteArrayResource(bytes);

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @GetMapping("/report/{lang}/{text}")
    public ResponseEntity<AnalysisDto> getReport(@PathVariable String text,
                                                 @PathVariable String lang,
                                                 @CurrentUser LocalUser user) {
        return ResponseEntity.ok(languageProcessorImpl.getReport(text, lang, user.getUser()));
    }
}

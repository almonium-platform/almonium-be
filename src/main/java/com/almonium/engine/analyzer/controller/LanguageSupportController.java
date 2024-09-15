package com.almonium.engine.analyzer.controller;

import com.almonium.engine.translator.model.enums.Language;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/lang")
@RequiredArgsConstructor
public class LanguageSupportController {

    @GetMapping
    public ResponseEntity<List<Language>> getAllSupportedLanguages() {
        return ResponseEntity.ok(List.of(Language.values()));
    }
}

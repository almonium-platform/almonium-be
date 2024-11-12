package com.almonium.analyzer.analyzer.controller.open;

import com.almonium.analyzer.translator.model.enums.Language;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class LanguageSupportController {

    @GetMapping("/supported-langs")
    public ResponseEntity<List<Language>> getAllSupportedLanguages() {
        return ResponseEntity.ok(List.of(Language.values()));
    }
}

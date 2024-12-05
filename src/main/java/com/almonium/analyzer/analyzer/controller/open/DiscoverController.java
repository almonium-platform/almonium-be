package com.almonium.analyzer.analyzer.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.service.FrequencyService;
import com.almonium.analyzer.translator.model.enums.Language;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/discover")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class DiscoverController {
    FrequencyService frequencyService;

    @GetMapping("/freq/{lang}/")
    public ResponseEntity<?> search(@PathVariable Language lang, @RequestParam String text) {
        return ResponseEntity.ok(frequencyService.getFrequency(lang, text));
    }
}

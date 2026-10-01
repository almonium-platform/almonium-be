package com.almonium.analyzer.analyzer.controller;

import com.almonium.analyzer.translator.dto.VoiceAvailabilityDto;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.analyzer.translator.service.VoiceCatalogue;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Learning")
@RestController
@RequestMapping("/lang/voices")
@RequiredArgsConstructor
public class VoiceCatalogueController {
    private final VoiceCatalogue catalogue;

    @GetMapping
    public List<VoiceAvailabilityDto> list() {
        return catalogue.list(null);
    }

    @GetMapping("/{language}")
    public List<VoiceAvailabilityDto> forLanguage(@PathVariable Language language) {
        return catalogue.list(language);
    }
}

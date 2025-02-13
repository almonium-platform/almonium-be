package com.almonium.info.controller.open;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.user.core.dto.response.InterestDto;
import com.almonium.user.core.service.InterestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/info")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StaticInfoController {
    InterestService interestService;

    @GetMapping("/languages/supported")
    public ResponseEntity<List<Language>> getSupportedLanguages() {
        return ResponseEntity.ok(List.of(Language.values()));
    }

    @GetMapping("/interests")
    public ResponseEntity<List<InterestDto>> getInterests() {
        return ResponseEntity.ok(interestService.getInterests());
    }
}

package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.request.KeepLanguageRequest;
import com.almonium.user.core.dto.request.TargetLanguagesSetupRequest;
import com.almonium.user.core.dto.request.UpdateLearnerRequest;
import com.almonium.user.core.dto.response.ActiveLanguagePolicy;
import com.almonium.user.core.dto.response.LearnerDto;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.ActiveLanguageService;
import com.almonium.user.core.service.LearnerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Learning", description = "Functionality for discovering, managing, and engaging with learning materials")
@RestController
@RequestMapping("/learners")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerController {
    LearnerService learnerService;
    ActiveLanguageService activeLanguageService;

    @PostMapping
    public ResponseEntity<List<LearnerDto>> createLearners(
            @Valid @RequestBody TargetLanguagesSetupRequest request, @Auth User user) {
        return ResponseEntity.ok(learnerService.createLearners(request.data(), user, false));
    }

    @PatchMapping("/{code}")
    public ResponseEntity<LearnerDto> updateLearner(
            @PathVariable Language code, @Auth UUID userId, @RequestBody UpdateLearnerRequest request) {
        return ResponseEntity.ok(learnerService.updateLearner(userId, code, request));
    }

    /** What the account may do with its languages: the downgrade sheet and the switcher both read this. */
    @GetMapping("/active-language-policy")
    public ResponseEntity<ActiveLanguagePolicy> activeLanguagePolicy(@Auth User user) {
        return ResponseEntity.ok(activeLanguageService.policyFor(user));
    }

    /** Records which language survives the downgrade. Nothing changes until the plan actually ends. */
    @PutMapping("/keep-on-downgrade")
    public ResponseEntity<ActiveLanguagePolicy> keepOnDowngrade(
            @Valid @RequestBody KeepLanguageRequest request, @Auth User user) {
        activeLanguageService.chooseWhatToKeep(user, request.language());
        return ResponseEntity.ok(activeLanguageService.policyFor(user));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteLearner(@PathVariable Language code, @Auth User user) {
        learnerService.deleteLearner(code, user.getId());
        return ResponseEntity.noContent().build();
    }
}

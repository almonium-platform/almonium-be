package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.InterestDto;
import com.almonium.user.core.dto.LanguageSetupRequest;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.service.OnboardingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/me/onboarding")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OnboardingController {
    OnboardingService onboardingService;

    @GetMapping("/interests")
    public ResponseEntity<List<InterestDto>> getInterests() {
        return ResponseEntity.ok(onboardingService.getInterests());
    }

    @PostMapping("/interests")
    public ResponseEntity<?> saveInterests(@Auth User user, @Valid @RequestBody List<Long> interests) {
        onboardingService.saveInterests(user, interests);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/step/{step}")
    public ResponseEntity<?> completeStep(@Auth User user, @PathVariable SetupStep step) {
        onboardingService.completeSimpleStep(user, step);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/langs")
    public ResponseEntity<Void> setupLanguages(@Valid @RequestBody LanguageSetupRequest request, @Auth User user) {
        onboardingService.setupLanguages(user, request);
        return ResponseEntity.noContent().build();
    }
}

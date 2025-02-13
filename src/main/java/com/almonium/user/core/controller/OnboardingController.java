package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.LearnerDto;
import com.almonium.user.core.dto.request.LanguageSetupRequest;
import com.almonium.user.core.dto.request.SaveInterestsRequest;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.model.enums.SetupStep;
import com.almonium.user.core.service.OnboardingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/onboarding")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class OnboardingController {
    OnboardingService onboardingService;

    @PatchMapping("/step/{step}")
    public ResponseEntity<Void> completeSimpleStep(@Auth User user, @PathVariable SetupStep step) {
        onboardingService.completeSimpleStep(user, step);
        return ResponseEntity.ok().build();
    }

    // steps with updating user data
    @PostMapping("/interests")
    public ResponseEntity<Void> setupInterests(@Auth User user, @Valid @RequestBody SaveInterestsRequest interests) {
        onboardingService.setupInterests(user, interests.ids());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/langs")
    public ResponseEntity<List<LearnerDto>> setupLanguages(
            @Valid @RequestBody LanguageSetupRequest request, @Auth User user) {
        return ResponseEntity.ok(onboardingService.setupLanguages(user, request));
    }
}

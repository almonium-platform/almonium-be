package com.almonium.user.core.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.user.core.dto.LearnerDto;
import com.almonium.user.core.dto.request.TargetLanguagesSetupRequest;
import com.almonium.user.core.dto.request.UpdateLearnerRequest;
import com.almonium.user.core.model.entity.User;
import com.almonium.user.core.service.LearnerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/learners")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearnerController {
    LearnerService learnerService;

    @PostMapping
    public ResponseEntity<List<LearnerDto>> createLearners(
            @Valid @RequestBody TargetLanguagesSetupRequest request, @Auth User user) {
        return ResponseEntity.ok(learnerService.createLearners(request.data(), user, false));
    }

    @PatchMapping("/{code}")
    public ResponseEntity<Void> updateLearner(
            @PathVariable Language code, @Auth Long userId, @RequestBody UpdateLearnerRequest request) {
        learnerService.updateLearner(userId, code, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteLearner(@PathVariable Language code, @Auth User user) {
        learnerService.deleteLearner(code, user.getId());
        return ResponseEntity.noContent().build();
    }
}

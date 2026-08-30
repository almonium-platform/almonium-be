package com.almonium.learning.rhythm.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.rhythm.dto.request.LearningActivityRequest;
import com.almonium.learning.rhythm.dto.request.RhythmTargetRequest;
import com.almonium.learning.rhythm.dto.response.RhythmResponse;
import com.almonium.learning.rhythm.service.LearningRhythmService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Learning", description = "Functionality for discovering, managing, and engaging with learning materials")
@RestController
@RequestMapping("/learning")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearningRhythmController {
    LearningRhythmService learningRhythmService;

    @GetMapping("/rhythm")
    public ResponseEntity<RhythmResponse> getRhythm(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate today,
            @Auth UUID userId) {
        return ResponseEntity.ok(learningRhythmService.getRhythm(userId, today));
    }

    @PutMapping("/rhythm/target")
    public ResponseEntity<Void> updateTarget(@Valid @RequestBody RhythmTargetRequest request, @Auth UUID userId) {
        learningRhythmService.updateTarget(userId, request.language(), request.target());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/activity")
    public ResponseEntity<Void> recordActivity(@Valid @RequestBody LearningActivityRequest request, @Auth UUID userId) {
        learningRhythmService.recordActivity(userId, request);
        return ResponseEntity.noContent().build();
    }
}

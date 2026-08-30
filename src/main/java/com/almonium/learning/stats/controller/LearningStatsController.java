package com.almonium.learning.stats.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.stats.dto.response.LearningStatsResponse;
import com.almonium.learning.stats.service.LearningStatsService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Learning", description = "Functionality for discovering, managing, and engaging with learning materials")
@RestController
@RequestMapping("/learning/stats")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class LearningStatsController {
    LearningStatsService learningStatsService;

    @GetMapping("/{language}")
    public ResponseEntity<LearningStatsResponse> getStats(@PathVariable Language language, @Auth User user) {
        return ResponseEntity.ok(learningStatsService.getStats(user, language));
    }
}

package com.almonium.infra.stats.controller;

import com.almonium.infra.stats.dto.LanguagePulse;
import com.almonium.infra.stats.dto.StatsReport;
import com.almonium.infra.stats.service.LanguagePulseService;
import com.almonium.infra.stats.service.StatsReportService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/ops/stats")
@RequiredArgsConstructor
public class OperationsStatsController {
    private final StatsReportService statsReportService;
    private final LanguagePulseService languagePulseService;

    /** Sign-ups, returning users, subscriptions by plan, grants, and founding-member slots, right now. */
    @GetMapping
    public ResponseEntity<StatsReport> report() {
        return ResponseEntity.ok(statsReportService.report());
    }

    /** Per language: learners, Discover lookups, how many the lexicon answered, and the words it lacked. */
    @GetMapping("/languages")
    public ResponseEntity<LanguagePulse> languages(@RequestParam(defaultValue = "7") @Min(1) @Max(90) int days) {
        return ResponseEntity.ok(languagePulseService.report(days));
    }
}

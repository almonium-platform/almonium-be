package com.almonium.infra.stats.controller;

import com.almonium.infra.stats.dto.StatsReport;
import com.almonium.infra.stats.service.StatsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/stats")
@RequiredArgsConstructor
public class OperationsStatsController {
    private final StatsReportService statsReportService;

    /** Sign-ups, returning users, subscriptions by plan, grants, and founding-member slots, right now. */
    @GetMapping
    public ResponseEntity<StatsReport> report() {
        return ResponseEntity.ok(statsReportService.report());
    }
}

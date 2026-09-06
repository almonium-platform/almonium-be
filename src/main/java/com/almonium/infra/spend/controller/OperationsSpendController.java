package com.almonium.infra.spend.controller;

import com.almonium.infra.spend.client.OpenAiCostsClient;
import com.almonium.infra.spend.dto.SpendReport;
import com.almonium.infra.spend.service.SpendReportService;
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
@RequestMapping("/ops/spend")
@RequiredArgsConstructor
public class OperationsSpendController {
    private final SpendReportService spendReportService;

    /** What the models cost over the last {@code days} days, estimated per feature and as billed. */
    @GetMapping
    public ResponseEntity<SpendReport> report(
            @RequestParam(defaultValue = "30") @Min(1) @Max(OpenAiCostsClient.MAX_DAYS) int days) {
        return ResponseEntity.ok(spendReportService.report(days));
    }
}

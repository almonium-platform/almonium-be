package com.almonium.learning.wanted.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.wanted.dto.request.AliasRequest;
import com.almonium.learning.wanted.dto.request.StartRequest;
import com.almonium.learning.wanted.dto.response.DemandDto;
import com.almonium.learning.wanted.service.DemandService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** /ops → Demand (W5), replacing the book-requests tab: wanted, under copyright, no match. */
@Validated
@RestController
@RequestMapping("/ops/demand")
@RequiredArgsConstructor
public class OperationsDemandController {
    private final DemandService demandService;

    @GetMapping
    public ResponseEntity<DemandDto> demand(@RequestParam(defaultValue = "30") @Min(1) @Max(365) int days) {
        return ResponseEntity.ok(demandService.demand(days));
    }

    /** Start, optionally with a free text for the book processor to fetch: {@code {"sourceId": "…"}}. */
    @PostMapping("/wanted/{catalogWorkId}/{language}/start")
    @RequireRecentLogin
    public ResponseEntity<Void> start(
            @Auth User operator,
            @PathVariable UUID catalogWorkId,
            @PathVariable Language language,
            @RequestBody(required = false) StartRequest request) {
        demandService.start(operator, catalogWorkId, language, request == null ? null : request.sourceId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wanted/{catalogWorkId}/{language}/decline")
    @RequireRecentLogin
    public ResponseEntity<Void> decline(
            @Auth User operator, @PathVariable UUID catalogWorkId, @PathVariable Language language) {
        demandService.decline(operator, catalogWorkId, language);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/aliases")
    @RequireRecentLogin
    public ResponseEntity<Void> alias(@Auth User operator, @Valid @RequestBody AliasRequest request) {
        demandService.alias(operator, request.query(), request.catalogWorkId());
        return ResponseEntity.noContent().build();
    }
}

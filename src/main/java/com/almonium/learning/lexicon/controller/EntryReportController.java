package com.almonium.learning.lexicon.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.lexicon.dto.request.EntryReportRequest;
import com.almonium.learning.lexicon.dto.response.EntryReportReceipt;
import com.almonium.learning.lexicon.service.EntryReportService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The flag on a dictionary word or sense. Members only: a report names who sent it, so a pattern can be read. */
@RestController
@RequestMapping("/lexicon/reports")
@RequiredArgsConstructor
public class EntryReportController {
    private final EntryReportService entryReportService;

    @PostMapping
    public ResponseEntity<EntryReportReceipt> report(
            @Auth User reporter, @Valid @RequestBody EntryReportRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entryReportService.report(reporter, request));
    }
}

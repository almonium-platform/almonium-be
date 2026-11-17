package com.almonium.learning.lexicon.controller;

import com.almonium.learning.lexicon.dto.request.EntryReportResolveRequest;
import com.almonium.learning.lexicon.dto.response.EntryReportGroup;
import com.almonium.learning.lexicon.service.EntryReportService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** /ops → Reports: the open dictionary reports grouped by sense and reason, most common word first. */
@RestController
@RequestMapping("/ops/lexicon/reports")
@RequiredArgsConstructor
public class OperationsEntryReportController {
    private final EntryReportService entryReportService;

    @GetMapping
    public ResponseEntity<List<EntryReportGroup>> queue() {
        return ResponseEntity.ok(entryReportService.queue());
    }

    /** Closes one group: the fix is made, or the reports were wrong. */
    @PostMapping("/resolve")
    public ResponseEntity<Map<String, Integer>> resolve(@Valid @RequestBody EntryReportResolveRequest request) {
        return ResponseEntity.ok(Map.of("resolved", entryReportService.resolve(request)));
    }
}

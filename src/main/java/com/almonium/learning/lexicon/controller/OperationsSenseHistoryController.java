package com.almonium.learning.lexicon.controller;

import com.almonium.learning.lexicon.dto.response.SenseHistoryResponse;
import com.almonium.learning.lexicon.service.SenseHistoryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Where a sense a card was saved from has been, version by version, for an operator tracing a card. */
@RestController
@RequestMapping("/ops/lexicon/senses")
@RequiredArgsConstructor
public class OperationsSenseHistoryController {
    private final SenseHistoryService historyService;

    @GetMapping("/{key}/history")
    public SenseHistoryResponse history(@PathVariable UUID key) {
        return historyService.history(key);
    }
}

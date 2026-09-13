package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.book.dto.request.ApproveTranslationRequest;
import com.almonium.learning.book.dto.response.TranslationJobDto;
import com.almonium.learning.book.dto.response.TranslationQueueDto;
import com.almonium.learning.book.service.TranslationJobService;
import com.almonium.learning.book.service.TranslationOrderService;
import com.almonium.user.core.model.entity.User;
import com.almonium.util.dto.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The /ops translation queue: pairs readers asked for, the jobs approved for them, and the month's spend. */
@RestController
@RequestMapping("/ops/books")
@RequiredArgsConstructor
public class OperationsTranslationRequestController {
    private final TranslationJobService translationJobService;
    private final TranslationOrderService translationOrderService;

    @GetMapping("/translation-requests")
    public ResponseEntity<TranslationQueueDto> queue() {
        return ResponseEntity.ok(translationJobService.queue());
    }

    /** Approving posts one job to the processor and writes the estimate to the ledger. */
    @PostMapping("/translation-requests/{bookId}/{language}/approve")
    @RequireRecentLogin
    public ResponseEntity<TranslationJobDto> approve(
            @Auth User operator,
            @PathVariable UUID bookId,
            @PathVariable Language language,
            @RequestBody(required = false) @Valid ApproveTranslationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(translationJobService.approve(operator, bookId, language, request));
    }

    /** Declining settles every open request for the pair, refunds each asker's slot, and tells them plainly. */
    @DeleteMapping("/translation-requests/{bookId}/{language}")
    @RequireRecentLogin
    public ResponseEntity<ApiResponse> decline(@PathVariable UUID bookId, @PathVariable Language language) {
        int declined = translationOrderService.declineTranslationOrders(bookId, language);
        return ResponseEntity.ok(new ApiResponse(true, "Declined %d request(s)".formatted(declined)));
    }

    @PostMapping("/translation-jobs/{jobId}/cancel")
    @RequireRecentLogin
    public ResponseEntity<TranslationJobDto> cancel(@PathVariable UUID jobId) {
        return ResponseEntity.ok(translationJobService.cancel(jobId));
    }
}

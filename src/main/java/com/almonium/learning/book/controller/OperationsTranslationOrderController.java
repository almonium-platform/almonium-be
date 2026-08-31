package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.service.TranslationOrderService;
import com.almonium.util.dto.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ops/books/{bookId}/language/{language}/orders")
@RequiredArgsConstructor
public class OperationsTranslationOrderController {
    private final TranslationOrderService translationOrderService;

    /** Declining settles every open request for the pair and refunds each asker's monthly slot. */
    @DeleteMapping
    public ResponseEntity<ApiResponse> decline(@PathVariable UUID bookId, @PathVariable Language language) {
        int declined = translationOrderService.declineTranslationOrders(bookId, language);
        return ResponseEntity.ok(new ApiResponse(true, "Declined %d request(s)".formatted(declined)));
    }
}

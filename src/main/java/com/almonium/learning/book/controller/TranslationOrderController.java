package com.almonium.learning.book.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.dto.response.TranslationRequestQuotaDto;
import com.almonium.learning.book.service.TranslationOrderService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TranslationOrderController {
    TranslationOrderService translationOrderService;

    @GetMapping("/orders")
    public ResponseEntity<List<TranslationOrderDto>> getTranslationOrders(@Auth User user) {
        return ResponseEntity.ok(translationOrderService.list(user));
    }

    @GetMapping("/orders/quota")
    public ResponseEntity<TranslationRequestQuotaDto> getTranslationOrderQuota(@Auth User user) {
        return ResponseEntity.ok(translationOrderService.quota(user));
    }

    @PostMapping("/{bookId}/language/{language}/orders")
    public ResponseEntity<TranslationOrderDto> createTranslationOrder(
            @Auth User user, @PathVariable UUID bookId, @PathVariable Language language) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(translationOrderService.createTranslationOrder(user, bookId, language));
    }

    @DeleteMapping("/{bookId}/language/{language}/orders")
    public ResponseEntity<Void> cancelTranslationOrder(
            @Auth UUID userId, @PathVariable UUID bookId, @PathVariable Language language) {

        boolean deleted = translationOrderService.deleteTranslationOrder(userId, bookId, language);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

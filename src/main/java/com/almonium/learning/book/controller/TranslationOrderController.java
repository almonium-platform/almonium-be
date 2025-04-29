package com.almonium.learning.book.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.service.TranslationOrderService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequestMapping("/books/{bookId}/language/{language}/orders")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TranslationOrderController {
    TranslationOrderService translationOrderService;

    @PostMapping
    public ResponseEntity<TranslationOrderDto> createTranslationOrder(
            @Auth User user, @PathVariable Long bookId, @PathVariable Language language) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(translationOrderService.createTranslationOrder(user, bookId, language));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelTranslationOrder(
            @Auth UUID userId, @PathVariable Long bookId, @PathVariable Language language) {

        boolean deleted = translationOrderService.deleteTranslationOrder(userId, bookId, language);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

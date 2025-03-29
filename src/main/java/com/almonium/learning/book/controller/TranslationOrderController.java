package com.almonium.learning.book.controller;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.TranslationOrderDto;
import com.almonium.learning.book.dto.response.TranslationOrderRequest;
import com.almonium.learning.book.service.TranslationOrderService;
import com.almonium.user.core.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/translation-orders")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class TranslationOrderController {
    TranslationOrderService translationOrderService;

    @PostMapping
    public ResponseEntity<TranslationOrderDto> createTranslationOrder(
            @Auth User user, @RequestBody TranslationOrderRequest orderRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(translationOrderService.createTranslationOrder(
                        user, orderRequest.bookId(), orderRequest.language()));
    }
}

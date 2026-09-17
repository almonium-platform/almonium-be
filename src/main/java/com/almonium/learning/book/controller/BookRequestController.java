package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.request.BookRequestAsk;
import com.almonium.learning.book.dto.response.BookLookupDto;
import com.almonium.learning.book.dto.response.BookRequestDto;
import com.almonium.learning.book.service.BookRequestService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** The ask sheet (G19): a lookup as the reader types, and the ask itself. Members only; a guest is sent to sign in first. */
@Validated
@RestController
@RequestMapping("/books/requests")
@RequiredArgsConstructor
public class BookRequestController {
    private final BookRequestService bookRequestService;

    @GetMapping("/lookup")
    public ResponseEntity<BookLookupDto> lookup(
            @Auth User user, @RequestParam @Size(max = 800) String q, @RequestParam Language language) {
        return ResponseEntity.ok(bookRequestService.lookup(q, language));
    }

    @PostMapping
    public ResponseEntity<BookRequestDto> ask(@Auth User user, @Valid @RequestBody BookRequestAsk ask) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRequestService.ask(user, ask));
    }
}

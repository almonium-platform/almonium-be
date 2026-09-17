package com.almonium.learning.book.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.book.dto.response.BookRequestQueueDto;
import com.almonium.learning.book.dto.response.BookRequestRow;
import com.almonium.learning.book.service.BookRequestService;
import com.almonium.user.core.model.entity.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The /ops book-request queue (G20); every decision takes the row's id and applies to every ask of the work. */
@RestController
@RequestMapping("/ops/books/requests")
@RequiredArgsConstructor
public class OperationsBookRequestController {
    private final BookRequestService bookRequestService;

    @GetMapping
    public ResponseEntity<BookRequestQueueDto> queue() {
        return ResponseEntity.ok(bookRequestService.queue());
    }

    /** "Add edition" or "New work": the row moves to In progress and the reviewer is sent to the editorial catalogue. */
    @PostMapping("/{id}/start")
    @RequireRecentLogin
    public ResponseEntity<BookRequestRow> start(@Auth User operator, @PathVariable UUID id) {
        return ResponseEntity.ok(bookRequestService.start(operator, id));
    }

    @PostMapping("/{id}/decline")
    @RequireRecentLogin
    public ResponseEntity<BookRequestRow> decline(@Auth User operator, @PathVariable UUID id) {
        return ResponseEntity.ok(bookRequestService.decline(operator, id));
    }
}

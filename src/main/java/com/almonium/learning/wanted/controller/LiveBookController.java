package com.almonium.learning.wanted.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.wanted.dto.request.LiveBookRequest;
import com.almonium.learning.wanted.dto.response.LiveBook;
import com.almonium.learning.wanted.service.LiveBookService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A book fetched while its reader waits. {@code POST /books/live} opens a catalogue work that is not on the shelf
 * and answers where it stands; the client asks {@code GET /books/live/{preparationId}} about once a second until it
 * is READY (open {@code editionSlug}) or FAILED.
 */
@RestController
@RequestMapping("/books/live")
@RequiredArgsConstructor
public class LiveBookController {
    private final LiveBookService liveBookService;

    @PostMapping
    public ResponseEntity<LiveBook> open(@Auth User user, @Valid @RequestBody LiveBookRequest request) {
        return ResponseEntity.ok(liveBookService.open(user, request));
    }

    @GetMapping("/{preparationId}")
    public ResponseEntity<LiveBook> status(@Auth User user, @PathVariable UUID preparationId) {
        return ResponseEntity.ok(liveBookService.status(preparationId));
    }
}

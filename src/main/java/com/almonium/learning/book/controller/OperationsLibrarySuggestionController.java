package com.almonium.learning.book.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.auth.common.annotation.RequireRecentLogin;
import com.almonium.learning.book.dto.request.PointToLibraryRequest;
import com.almonium.learning.book.dto.response.LibrarySuggestionQueueDto;
import com.almonium.learning.book.dto.response.StoredFile;
import com.almonium.learning.book.service.LibrarySuggestionService;
import com.almonium.learning.book.service.LibrarySuggestionService.LibrarySuggestionRowUpdate;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The /ops library-suggestion queue; every decision takes the row's id and applies to the whole work. */
@RestController
@RequestMapping("/ops/books/library-suggestions")
@RequiredArgsConstructor
public class OperationsLibrarySuggestionController {
    private final LibrarySuggestionService librarySuggestionService;

    @GetMapping
    public ResponseEntity<LibrarySuggestionQueueDto> queue() {
        return ResponseEntity.ok(librarySuggestionService.queue());
    }

    @PostMapping("/{id}/accept")
    @RequireRecentLogin
    public ResponseEntity<LibrarySuggestionRowUpdate> accept(@Auth User operator, @PathVariable UUID id) {
        return ResponseEntity.ok(librarySuggestionService.accept(operator, id));
    }

    @PostMapping("/{id}/decline")
    @RequireRecentLogin
    public ResponseEntity<LibrarySuggestionRowUpdate> decline(@Auth User operator, @PathVariable UUID id) {
        return ResponseEntity.ok(librarySuggestionService.decline(operator, id));
    }

    @PostMapping("/{id}/point-to-library")
    @RequireRecentLogin
    public ResponseEntity<LibrarySuggestionRowUpdate> pointToLibrary(
            @Auth User operator, @PathVariable UUID id, @Valid @RequestBody PointToLibraryRequest request) {
        return ResponseEntity.ok(librarySuggestionService.pointToLibrary(operator, id, request.bookId()));
    }

    @PostMapping("/{id}/cancel")
    @RequireRecentLogin
    public ResponseEntity<LibrarySuggestionRowUpdate> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(librarySuggestionService.cancel(id));
    }

    /** The owner's upload, read-only, for the reviewer. */
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> file(@PathVariable UUID id) {
        StoredFile file = librarySuggestionService.file(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.contentType()));
        headers.setContentDisposition(
                ContentDisposition.attachment().filename(file.filename()).build());
        return ResponseEntity.ok().headers(headers).body(file.content());
    }
}

package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.BookImportDto;
import com.almonium.learning.book.dto.response.BookImportQuotaDto;
import com.almonium.learning.book.service.UserBookImportService;
import com.almonium.user.core.model.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/book-imports")
@RequiredArgsConstructor
public class BookImportController {
    private final UserBookImportService importService;

    @PostMapping
    public ResponseEntity<BookImportDto> create(
            @Auth User user,
            @RequestPart("file") MultipartFile file,
            @RequestParam @NotBlank String title,
            @RequestParam @NotBlank String author,
            @RequestParam(defaultValue = "") String description,
            @RequestParam Language language,
            @RequestParam(required = false) @Min(1) @Max(9999) Integer publicationYear) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(importService.create(user, file, title, author, description, language, publicationYear));
    }

    @GetMapping
    public ResponseEntity<List<BookImportDto>> list(@Auth User user) {
        return ResponseEntity.ok(importService.list(user));
    }

    @GetMapping("/quota")
    public ResponseEntity<BookImportQuotaDto> quota(@Auth User user) {
        return ResponseEntity.ok(importService.quota(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookImportDto> get(@Auth User user, @PathVariable UUID id) {
        return ResponseEntity.ok(importService.get(user, id));
    }

    @GetMapping("/{id}/text")
    public ResponseEntity<byte[]> text(@Auth User user, @PathVariable UUID id) {
        return ResponseEntity.ok(importService.text(user, id));
    }
}

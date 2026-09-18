package com.almonium.learning.book.controller;

import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.request.CertificateVisibilityRequest;
import com.almonium.learning.book.dto.response.BookCertificateDto;
import com.almonium.learning.book.service.BookCertificateService;
import com.almonium.learning.book.service.CertificateImageRenderer;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** The owner's side of the certificate (design K3, K4): issued at the last page, kept by the finished book's tile. */
@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequestMapping("/books/{bookId}/certificate")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookCertificateController {
    BookCertificateService certificateService;
    CertificateImageRenderer imageRenderer;

    /** Reaching the end of the last chapter: issues the certificate, or returns the one already issued. */
    @PostMapping
    public ResponseEntity<BookCertificateDto> issue(@Auth User user, @PathVariable UUID bookId) {
        return ResponseEntity.ok(certificateService.issue(user, bookId));
    }

    @GetMapping
    public ResponseEntity<BookCertificateDto> get(@Auth User user, @PathVariable UUID bookId) {
        return ResponseEntity.ok(certificateService.get(user, bookId));
    }

    @PutMapping("/visibility")
    public ResponseEntity<BookCertificateDto> setVisibility(
            @Auth User user, @PathVariable UUID bookId, @RequestBody CertificateVisibilityRequest request) {
        return ResponseEntity.ok(certificateService.setPublicPage(user, bookId, request.publicPage()));
    }

    /** "Save as image": the 1200×630 PNG, whether or not the public page is on. */
    @GetMapping("/image")
    public ResponseEntity<byte[]> image(@Auth User user, @PathVariable UUID bookId) {
        BookCertificateDto certificate = certificateService.get(user, bookId);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"almonium-" + certificate.editionSlug() + ".png\"")
                .body(imageRenderer.render(certificate));
    }
}

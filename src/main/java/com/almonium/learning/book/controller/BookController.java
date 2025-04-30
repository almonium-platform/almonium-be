package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.BookDetails;
import com.almonium.learning.book.dto.response.BookMiniDetails;
import com.almonium.learning.book.dto.response.BookshelfViewDto;
import com.almonium.learning.book.service.BookService;
import com.almonium.user.core.model.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @GetMapping("/{bookId}/language/{language}")
    public ResponseEntity<BookDetails> getMyBook(
            @Auth User user, @PathVariable Language language, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(user, language, bookId));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookMiniDetails> getBookInfo(@Auth UUID userId, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(userId, bookId));
    }

    @GetMapping("/{bookId}/parallel/{language}")
    public ResponseEntity<byte[]> getParallelBook(
            @Auth User user, @PathVariable Language language, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getParallelBook(user, language, bookId));
    }

    @GetMapping("/{bookId}/text")
    public ResponseEntity<byte[]> getText(@Auth User user, @PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getText(user, bookId));
    }

    @PostMapping("/{bookId}/language/{language}/favorite")
    public ResponseEntity<?> addToFavorites(
            @Auth User user, @PathVariable Language language, @PathVariable Long bookId) {
        bookService.addToFavorites(user, bookId, language);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{bookId}/language/{language}/favorite")
    public ResponseEntity<Void> deleteFromFavorites(
            @Auth User user, @PathVariable Language language, @PathVariable Long bookId) {
        boolean deleted = bookService.deleteFromFavorites(user, bookId, language);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<BookshelfViewDto> getMyBooks(
            @Auth User user,
            @PathVariable Language language,
            @RequestParam(required = false) Boolean includeTranslations) {
        return ResponseEntity.ok(bookService.getBooksInLanguage(user, language, includeTranslations));
    }

    @DeleteMapping("/{bookId}/progress")
    public ResponseEntity<?> deleteBookProgress(@Auth User user, @PathVariable Long bookId) {
        boolean success = bookService.deleteBookProgress(user, bookId);
        if (success) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{bookId}/progress")
    public ResponseEntity<?> saveBookProgress(
            @Auth User user, @PathVariable Long bookId, @RequestParam int percentage) {
        bookService.saveBookProgress(user, bookId, percentage);
        return ResponseEntity.noContent().build();
    }
}

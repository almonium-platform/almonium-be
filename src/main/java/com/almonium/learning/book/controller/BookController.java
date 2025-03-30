package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.BookDetails;
import com.almonium.learning.book.dto.response.BookshelfViewDto;
import com.almonium.learning.book.service.BookService;
import com.almonium.user.core.model.entity.User;
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

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @GetMapping("/language/{language}/{id}")
    public ResponseEntity<BookDetails> getMyBooks(
            @Auth User user, @PathVariable Language language, @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(user, language, id));
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

    @DeleteMapping("/language/{language}/{bookId}/progress")
    public ResponseEntity<?> deleteBookProgress(
            @Auth User user, @PathVariable Language language, @PathVariable Long bookId) {
        bookService.deleteBookProgress(user, language, bookId);
        return ResponseEntity.noContent().build();
    }
}

package com.almonium.learning.book.controller;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.auth.common.annotation.Auth;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.service.BookService;
import com.almonium.user.core.model.entity.User;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {
    BookService bookService;

    @GetMapping("/language/{language}")
    public ResponseEntity<List<BookDto>> getMyBooks(@Auth User user, @PathVariable Language language) {
        return ResponseEntity.ok(bookService.getBooksWithProgressInLanguage(user, language));
    }

    @DeleteMapping("/language/{language}/{bookId}/progress")
    public ResponseEntity<?> deleteBookProgress(
            @Auth User user, @PathVariable Language language, @PathVariable UUID bookId) {
        bookService.deleteBookProgress(user, language, bookId);
        return ResponseEntity.noContent().build();
    }
}

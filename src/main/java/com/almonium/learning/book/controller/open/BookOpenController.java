package com.almonium.learning.book.controller.open;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.dto.response.BookDto;
import com.almonium.learning.book.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Books", description = "Operations related to discovering, managing, and interacting with books.")
@RestController
@RequestMapping("/public/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookOpenController {
    BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @GetMapping("/lang/{language}")
    public ResponseEntity<List<BookDto>> getMyBooks(@PathVariable Language language) {
        return ResponseEntity.ok(bookService.getBooksInLanguage(language));
    }
}

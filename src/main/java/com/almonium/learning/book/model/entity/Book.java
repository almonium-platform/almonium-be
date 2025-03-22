package com.almonium.learning.book.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.analyzer.analyzer.model.enums.CEFR;
import com.almonium.analyzer.translator.model.enums.Language;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Book {
    @Id
    Long id;

    // Reference to original book (null if this IS the original)
    @ManyToOne
    @JoinColumn(name = "original_book_id")
    Book originalBook;

    String title;
    String author;
    int publicationYear;
    String coverImageUrl;
    int wordCount;
    double rating;

    @Enumerated(EnumType.STRING)
    Language language;

    @Enumerated(EnumType.STRING)
    CEFR levelFrom;

    @Enumerated(EnumType.STRING)
    CEFR levelTo;

    String description;

    // For translations
    String translator;
}

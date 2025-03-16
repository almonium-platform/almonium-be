package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookWithProgress;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, UUID> {
    List<Book> findByLanguage(Language language);

    @Query("SELECT b as book, lbp as progress FROM Book b "
            + "LEFT JOIN LearnerBookProgress lbp ON b.id = lbp.book.id AND lbp.learner.id = :learnerId "
            + "WHERE b.language = :language "
            + "ORDER BY CASE WHEN lbp IS NOT NULL THEN 0 ELSE 1 END")
    List<BookWithProgress> findBooksWithProgressByLanguageAndLearnerId(Language language, UUID learnerId);
}

package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookWithTranslationStatus;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguage(Language language);

    @Query(
            """
        SELECT b.id as id,
               b.title as title,
               b.author as author,
               b.publicationYear as publicationYear,
               b.coverImageUrl as coverImageUrl,
               b.wordCount as wordCount,
               b.rating as rating,
               b.language as language,
               b.levelFrom as levelFrom,
               b.levelTo as levelTo,
               bp.progressPercentage as progressPercentage,
               case when exists (select 1 from Book t where t.originalBook.id = b.id and t.language = :language)
                    or (b.originalBook is not null and b.language = :language) then true else false end as hasTranslation,
               case when exists (select 1 from Book t where (t.originalBook.id = b.id or t.id = b.originalBook.id)
                    and t.language in :fluentLanguages) then true else false end as hasParallelTranslation,
               case when b.originalBook is not null then true else false end as isTranslation
        from Book b
        join LearnerBookProgress bp on b.id = bp.book.id
        where bp.learner.id = :learnerId
        and b.language = :language
        order by bp.lastReadAt desc
    """)
    List<BookWithTranslationStatus> findBooksInProgressByLearner(
            UUID learnerId, Language language, Collection<Language> fluentLanguages);

    @Query(
            """
        SELECT b.id as id,
               b.title as title,
               b.author as author,
               b.publicationYear as publicationYear,
               b.coverImageUrl as coverImageUrl,
               b.wordCount as wordCount,
               b.rating as rating,
               b.language as language,
               b.levelFrom as levelFrom,
               b.levelTo as levelTo,
               null as progressPercentage,
               case when exists (select 1 from Book t where t.originalBook.id = b.id and t.language = :language)
                    or (b.originalBook is not null and b.language = :language) then true else false end as hasTranslation,
               case when exists (select 1 from Book t where (t.originalBook.id = b.id or t.id = b.originalBook.id)
                    and t.language in :fluentLanguages) then true else false end as hasParallelTranslation,
               case when b.originalBook is not null then true else false end as isTranslation
        from Book b
        where b.language = :language
        and ((:includeTranslations = true) or (b.originalBook is null))
        and not exists (
            select 1 from LearnerBookProgress lbp
            where lbp.book.id = b.id
            and lbp.learner.id = :learnerId
        )
        order by b.rating desc
    """)
    List<BookWithTranslationStatus> findAvailableBooks(
            Language language, UUID learnerId, Collection<Language> fluentLanguages, boolean includeTranslations);
}

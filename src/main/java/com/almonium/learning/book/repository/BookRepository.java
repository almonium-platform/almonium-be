package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.Book;
import com.almonium.learning.book.model.entity.BookDetailsProjection;
import com.almonium.learning.book.model.entity.BookMiniProjection;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguage(Language language);

    @Query(
            """
        select b.id as id,
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
               b.description as description,
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
    List<BookDetailsProjection> findBooksInProgressByLearner(
            UUID learnerId, Language language, Collection<Language> fluentLanguages);

    @Query(
            """
        select b.id as id,
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
               b.description as description,
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
        and not exists (
            select 1 from BookFavorite bf
            where bf.book.id = b.id
            and bf.learner.id = :learnerId
        )
        order by b.rating desc
    """)
    List<BookDetailsProjection> findAvailableBooks(
            Language language, UUID learnerId, Collection<Language> fluentLanguages, boolean includeTranslations);

    @Query(
            """
        select b.id as id,
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
               b.description as description,
               case when exists (select 1 from Book t where t.originalBook.id = b.id and t.language = :language)
                    or (b.originalBook is not null and b.language = :language) then true else false end as hasTranslation,
               case when exists (select 1 from Book t where (t.originalBook.id = b.id or t.id = b.originalBook.id)
                    and t.language in :fluentLanguages) then true else false end as hasParallelTranslation,
               case when b.originalBook is not null then true else false end as isTranslation
        from Book b
        join BookFavorite bf on b.id = bf.book.id
        where bf.learner.id = :learnerId
        and b.language = :language
        and ((:includeTranslations = true) or (b.originalBook is null))
        order by b.rating desc
    """)
    List<BookDetailsProjection> findFavoriteBooks(
            Language language, UUID learnerId, Collection<Language> fluentLanguages, boolean includeTranslations);

    @Query(
            """
        select b.id as id,
               b.title as title,
               b.author as author,
               b.publicationYear as publicationYear,
               b.coverImageUrl as coverImageUrl,
               b.wordCount as wordCount,
               b.rating as rating,
               b.language as language,
               b.levelFrom as levelFrom,
               b.levelTo as levelTo,
               case when ob.id is not null then ob.language else b.language end as originalLanguage,
               case when ob.id is not null then ob.id else b.id end as originalId,
               b.description as description,
               b.translator as translator,
               (select bp.progressPercentage from LearnerBookProgress bp
                where bp.book.id = b.id and bp.learner.id = :learnerId) as progressPercentage,
               case when exists (select 1 from Book t where t.originalBook.id = b.id)
                    or b.originalBook is not null then true else false end as hasTranslation,
               case when exists (select 1 from Book t where (t.originalBook.id = b.id or t.id = b.originalBook.id)
                    and t.language in :fluentLanguages) then true else false end as hasParallelTranslation,
               case when b.originalBook is not null then true else false end as isTranslation
        from Book b
        left join b.originalBook ob
        where b.id = :bookId
    """)
    Optional<BookDetailsProjection> findBookDtoById(Long bookId, UUID learnerId, Collection<Language> fluentLanguages);

    @Query(
            """
            select b.id as id, b.language as language
            from Book b
            where b.id = :bookId
               or (b.originalBook is not null and b.originalBook.id = :bookId)
               or (b.originalBook is null and exists (
                    select 1 from Book t
                    where t.originalBook.id = b.id and t.id = :bookId
                  ))
        """)
    List<BookMiniProjection> findAvailableLanguagesForBook(Long bookId);
}

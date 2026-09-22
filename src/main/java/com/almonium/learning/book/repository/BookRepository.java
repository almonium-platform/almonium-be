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

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> findByEditionSlug(String editionSlug);

    // Native, so it sees withdrawn rows too: publishing an edition again must
    // revive the book readers already have progress on, not duplicate it.
    @Query(value = "select * from book where edition_slug = :editionSlug", nativeQuery = true)
    Optional<Book> findAnyByEditionSlug(String editionSlug);

    List<Book> findByLanguage(Language language);

    /** Every edition of a work, whatever its language; the caller matches the author. */
    List<Book> findByWorkSlug(String workSlug);

    // Withdrawn translations are already invisible here, so this counts only the
    // ones that would be left pointing at a book nobody can read.
    @Query("select count(t) from Book t where t.originalBook.id = :bookId")
    long countTranslationsOf(UUID bookId);

    @Query("""
        select b.id as id,
               b.editionSlug as editionSlug,
               b.workSlug as workSlug,
               b.title as title,
               b.author as author,
               b.description as description,
               b.publicationYear as publicationYear,
               b.coverUrl as coverUrl,
               b.wordCount as wordCount,
               b.language as language,
               b.cefrLevel as cefrLevel,
               b.adaptsTo as adaptsTo,
               b.editionType as editionType,
               b.literaryRegister as literaryRegister,
               bp.progressPercentage as progressPercentage,
               bp.currentChapter as currentChapter,
               b.chapterCount as chapterCount,
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

    @Query("""
        select b.id as id,
               b.editionSlug as editionSlug,
               b.workSlug as workSlug,
               b.title as title,
               b.author as author,
               b.description as description,
               b.publicationYear as publicationYear,
               b.coverUrl as coverUrl,
               b.wordCount as wordCount,
               b.language as language,
               b.cefrLevel as cefrLevel,
               b.adaptsTo as adaptsTo,
               b.editionType as editionType,
               b.literaryRegister as literaryRegister,
               null as progressPercentage,
               null as currentChapter,
               b.chapterCount as chapterCount,
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
        order by b.title asc
    """)
    List<BookDetailsProjection> findAvailableBooks(
            Language language, UUID learnerId, Collection<Language> fluentLanguages, boolean includeTranslations);

    @Query("""
        select b.id as id,
               b.editionSlug as editionSlug,
               b.workSlug as workSlug,
               b.title as title,
               b.author as author,
               b.description as description,
               b.publicationYear as publicationYear,
               b.coverUrl as coverUrl,
               b.wordCount as wordCount,
               b.language as language,
               b.cefrLevel as cefrLevel,
               b.adaptsTo as adaptsTo,
               b.editionType as editionType,
               b.literaryRegister as literaryRegister,
               null as progressPercentage,
               null as currentChapter,
               b.chapterCount as chapterCount,
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
        order by b.title asc
    """)
    List<BookDetailsProjection> findFavoriteBooks(
            Language language, UUID learnerId, Collection<Language> fluentLanguages, boolean includeTranslations);

    @Query("""
        select b.id as id,
               b.editionSlug as editionSlug,
               b.workSlug as workSlug,
               b.title as title,
               b.author as author,
               b.description as description,
               b.publicationYear as publicationYear,
               b.coverUrl as coverUrl,
               b.wordCount as wordCount,
               b.language as language,
               b.cefrLevel as cefrLevel,
               b.adaptsTo as adaptsTo,
               b.editionType as editionType,
               b.literaryRegister as literaryRegister,
               case when ob.id is not null then ob.language else b.language end as originalLanguage,
               case when ob.id is not null then ob.id else b.id end as originalId,
               case when ob.id is not null and ob.title <> b.title then ob.title else null end as originalTitle,
               b.translator as translator,
               (select bp.progressPercentage from LearnerBookProgress bp
                where bp.book.id = b.id and bp.learner.id = :learnerId) as progressPercentage,
               (select bp.currentChapter from LearnerBookProgress bp
                where bp.book.id = b.id and bp.learner.id = :learnerId) as currentChapter,
               b.chapterCount as chapterCount,
               case when exists (select 1 from Book t where t.originalBook.id = b.id)
                    or b.originalBook is not null then true else false end as hasTranslation,
               case when exists (select 1 from Book t where (t.originalBook.id = b.id or t.id = b.originalBook.id)
                    and t.language in :fluentLanguages) then true else false end as hasParallelTranslation,
               case when b.originalBook is not null then true else false end as isTranslation
        from Book b
        left join b.originalBook ob
        where b.id = :bookId
    """)
    Optional<BookDetailsProjection> findBookDtoById(UUID bookId, UUID learnerId, Collection<Language> fluentLanguages);

    @Query("""
            select b.id as id, b.editionSlug as editionSlug, b.language as language,
                   b.editionType as editionType, cast(b.cefrLevel as string) as cefrLevel,
                   b.literaryRegister as literaryRegister,
                   source.editionSlug as sourceEditionSlug, b.editionNote as editionNote
            from Book b
            left join b.originalBook source
            where b.workSlug = (select selected.workSlug from Book selected where selected.id = :bookId)
            order by b.language, b.cefrLevel, b.editionSlug
        """)
    List<BookMiniProjection> findAvailableLanguagesForBook(UUID bookId);
}

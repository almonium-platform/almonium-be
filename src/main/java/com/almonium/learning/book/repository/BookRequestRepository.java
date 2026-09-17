package com.almonium.learning.book.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.book.model.entity.BookRequest;
import com.almonium.learning.book.model.enums.BookRequestStatus;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRequestRepository extends JpaRepository<BookRequest, UUID> {
    boolean existsByUserIdAndNormalizedKeyAndLanguageAndStatusIn(
            UUID userId, String normalizedKey, Language language, Collection<BookRequestStatus> statuses);

    List<BookRequest> findByStatusIn(Collection<BookRequestStatus> statuses);

    List<BookRequest> findByNormalizedKeyAndLanguageAndStatusIn(
            String normalizedKey, Language language, Collection<BookRequestStatus> statuses);

    List<BookRequest> findByWorkSlugAndLanguageAndStatusIn(
            String workSlug, Language language, Collection<BookRequestStatus> statuses);

    long countByStatus(BookRequestStatus status);

    @Query("""
        select count(distinct r.user.id) from BookRequest r
        where r.normalizedKey = :normalizedKey and r.language = :language and r.status in :statuses
    """)
    long countAskers(String normalizedKey, Language language, Collection<BookRequestStatus> statuses);
}

package com.almonium.learning.wanted.repository;

import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.learning.wanted.model.entity.Want;
import com.almonium.learning.wanted.model.enums.WantStatus;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WantRepository extends JpaRepository<Want, UUID> {
    Optional<Want> findByUserIdAndCatalogWorkIdAndLanguage(UUID userId, UUID catalogWorkId, Language language);

    List<Want> findByCatalogWorkIdAndLanguageAndStatusIn(
            UUID catalogWorkId, Language language, Collection<WantStatus> statuses);

    List<Want> findByUserIdAndStatusIn(UUID userId, Collection<WantStatus> statuses);

    /** One work in one language, as the queue counts it: how many distinct readers, and since when. */
    interface WantGroup {
        UUID getCatalogWorkId();

        Language getLanguage();

        WantStatus getStatus();

        long getReaders();

        Instant getFirstWantedAt();

        Instant getLastWantedAt();
    }

    @Query("""
            select w.catalogWork.id as catalogWorkId, w.language as language, w.status as status,
                   count(distinct w.user.id) as readers, min(w.createdAt) as firstWantedAt,
                   max(w.createdAt) as lastWantedAt
            from Want w
            where w.status in :statuses
            group by w.catalogWork.id, w.language, w.status
            """)
    List<WantGroup> groups(Collection<WantStatus> statuses);

    /** Works in a language that arrived since a moment, newest first, for "Recently arrived". */
    @Query("""
            select w from Want w
            where w.status = com.almonium.learning.wanted.model.enums.WantStatus.ARRIVED and w.arrivedAt >= :since
            order by w.arrivedAt desc
            """)
    List<Want> arrivedSince(Instant since);

    /** Distinct readers per work for one language among standing wants, for search results. */
    interface WorkReaders {
        UUID getCatalogWorkId();

        long getReaders();
    }

    @Query("""
            select w.catalogWork.id as catalogWorkId, count(distinct w.user.id) as readers
            from Want w
            where w.catalogWork.id in :workIds and w.language = :language
              and w.status in (com.almonium.learning.wanted.model.enums.WantStatus.OPEN,
                               com.almonium.learning.wanted.model.enums.WantStatus.IN_PROGRESS)
            group by w.catalogWork.id
            """)
    List<WorkReaders> readers(Collection<UUID> workIds, Language language);

    @Query("""
            select w.catalogWork.id from Want w
            where w.user.id = :userId and w.catalogWork.id in :workIds and w.language = :language
              and w.status in (com.almonium.learning.wanted.model.enums.WantStatus.OPEN,
                               com.almonium.learning.wanted.model.enums.WantStatus.IN_PROGRESS)
            """)
    List<UUID> wantedBy(UUID userId, Collection<UUID> workIds, Language language);
}

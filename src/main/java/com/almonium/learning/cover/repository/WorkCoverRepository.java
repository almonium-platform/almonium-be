package com.almonium.learning.cover.repository;

import com.almonium.learning.cover.dto.CoverArt;
import com.almonium.learning.cover.dto.CoverRow;
import com.almonium.learning.cover.model.WorkCover;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkCoverRepository extends JpaRepository<WorkCover, String> {
    /** The approved covers without their bytes: what the shelf needs to know before it asks for an image. */
    @Query("""
        select new com.almonium.learning.cover.dto.CoverArt(c.workSlug, c.focusX, c.focusY)
        from WorkCover c
        where c.status = com.almonium.learning.cover.model.CoverStatus.APPROVED
        order by c.workSlug
        """)
    List<CoverArt> approved();

    @Query("""
        select new com.almonium.learning.cover.dto.CoverRow(
            c.workSlug, c.status, c.sourcePageUrl, c.artist, c.licence, c.licenceBasis, c.reason, c.model, c.checkedAt)
        from WorkCover c
        order by c.checkedAt desc
        """)
    List<CoverRow> rows();

    @Query("select c.workSlug from WorkCover c")
    List<String> workSlugs();
}

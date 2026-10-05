package com.almonium.learning.rights.repository;

import com.almonium.learning.rights.model.entity.WorkRightsBlock;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface WorkRightsBlockRepository extends JpaRepository<WorkRightsBlock, WorkRightsBlock.Key> {
    List<WorkRightsBlock> findByWorkSlug(String workSlug);

    @Modifying
    @Query("delete from WorkRightsBlock b where b.workSlug = :workSlug")
    void deleteByWorkSlug(String workSlug);
}

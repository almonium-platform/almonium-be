package com.almonium.learning.catalog.repository;

import com.almonium.learning.catalog.model.entity.CatalogWork;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogWorkRepository extends JpaRepository<CatalogWork, UUID> {
    List<CatalogWork> findByWorkSlug(String workSlug);

    Optional<CatalogWork> findByWikidataId(String wikidataId);
}

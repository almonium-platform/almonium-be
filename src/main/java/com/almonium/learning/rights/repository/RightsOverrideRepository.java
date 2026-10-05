package com.almonium.learning.rights.repository;

import com.almonium.learning.rights.model.entity.RightsOverride;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RightsOverrideRepository extends JpaRepository<RightsOverride, UUID> {
    List<RightsOverride> findByWorkSlug(String workSlug);

    Optional<RightsOverride> findByWorkSlugAndCountry(String workSlug, String country);
}

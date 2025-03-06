package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.Avatar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, UUID> {
    List<Avatar> findAllByProfileId(UUID profileId);

    Optional<Avatar> findByProfileIdAndUrl(UUID profileId, String url);

    void deleteAllByProfileId(UUID profileId);
}

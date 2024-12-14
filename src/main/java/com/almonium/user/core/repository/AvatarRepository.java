package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.Avatar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    List<Avatar> findAllByProfileId(Long profileId);

    Optional<Avatar> findByProfileIdAndUrl(Long profileId, String url);

    void deleteAllByProfileId(Long profileId);
}

package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.Learner;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LearnerRepository extends JpaRepository<Learner, Long> {

    @Query("SELECT l FROM Learner l LEFT JOIN FETCH l.targetLangs WHERE l.id = :id")
    Optional<Learner> findLearnerWithTargetLangs(long id);
}

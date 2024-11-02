package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.Learner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearnerRepository extends JpaRepository<Learner, Long> {}

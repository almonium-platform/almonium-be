package com.almonium.user.core.repository;

import com.almonium.user.core.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {}

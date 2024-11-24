package com.almonium.subscription.repository;

import com.almonium.subscription.model.entity.Insider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsiderRepository extends JpaRepository<Insider, Long> {}

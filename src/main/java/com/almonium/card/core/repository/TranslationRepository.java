package com.almonium.card.core.repository;

import com.almonium.card.core.model.entity.Translation;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationRepository extends JpaRepository<Translation, UUID> {}

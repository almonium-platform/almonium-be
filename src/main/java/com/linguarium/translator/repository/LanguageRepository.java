package com.linguarium.translator.repository;

import com.linguarium.translator.model.Language;
import com.linguarium.translator.model.LanguageEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    Optional<LanguageEntity> findByCode(Language code);

    default LanguageEntity getEnglish() {
        return this.findByCode(Language.ENGLISH).orElseThrow(() -> new EntityNotFoundException("English not found in DB"));
    }
}

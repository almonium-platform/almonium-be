package com.linguatool.repository;

import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.lang.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    Optional<LanguageEntity> findByCode(Language code);

    default LanguageEntity getEnglish() {
        return this.findByCode(Language.ENGLISH).orElseThrow();
    }

    default LanguageEntity getUkrainian() {
        return this.findByCode(Language.UKRAINIAN).orElseThrow();
    }

    default LanguageEntity getRussian() {
        return this.findByCode(Language.RUSSIAN).orElseThrow();
    }

    default LanguageEntity getGerman() {
        return this.findByCode(Language.GERMAN).orElseThrow();
    }

}

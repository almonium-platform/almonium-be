package com.linguatool.repository;

import com.linguatool.model.entity.lang.LanguageEntity;
import com.linguatool.model.entity.user.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    Optional<LanguageEntity> findByCode(Language code);

    default LanguageEntity getEnglish() {
        return this.findByCode(Language.ENGLISH).orElseThrow();
    }

}

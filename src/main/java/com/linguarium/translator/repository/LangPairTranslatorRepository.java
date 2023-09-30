package com.linguarium.translator.repository;

import com.linguarium.translator.model.LangPairTranslatorMapping;
import com.linguarium.translator.model.TranslatorMappingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LangPairTranslatorRepository extends JpaRepository<LangPairTranslatorMapping, TranslatorMappingKey> {

    @Query("SELECT l.translatorId FROM LangPairTranslatorMapping l WHERE l.sourceLangId = :sourceLangId AND l.targetLangId = :targetLangId ORDER BY l.priority")
    List<Long> getBySourceLangAndTargetLang(@Param("sourceLangId") long sourceLangId, @Param("targetLangId") long targetLangId);
}

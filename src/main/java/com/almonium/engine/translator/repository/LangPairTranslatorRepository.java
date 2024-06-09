package com.almonium.engine.translator.repository;

import com.almonium.engine.translator.model.entity.LangPairTranslatorMapping;
import com.almonium.engine.translator.model.entity.pk.TranslatorMappingKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LangPairTranslatorRepository extends JpaRepository<LangPairTranslatorMapping, TranslatorMappingKey> {
    @Query(
            """
            select l.translatorId
            from LangPairTranslatorMapping l
            where l.sourceLang = :sourceLang
            and l.targetLang = :targetLang
            order by l.priority
            """)
    List<Long> getBySourceLangAndTargetLang(
            @Param("sourceLang") String sourceLang, @Param("targetLang") String targetLang);
}

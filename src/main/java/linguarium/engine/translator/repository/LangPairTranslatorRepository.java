package linguarium.engine.translator.repository;

import java.util.List;
import linguarium.engine.translator.model.LangPairTranslatorMapping;
import linguarium.engine.translator.model.TranslatorMappingKey;
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

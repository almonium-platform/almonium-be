package com.linguatool.repository;

import com.linguatool.model.entity.lang.LangPairTranslator;
import com.linguatool.model.entity.lang.LangPairTranslatorKey;
import com.linguatool.model.entity.lang.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LangPairTranslatorRepository extends JpaRepository<LangPairTranslator, LangPairTranslatorKey> {

    @Query(value = "select translator_id from lang_pair_translator where lang_from_id =?1 and lang_to_id = ?2 order by priority",
            nativeQuery = true)
    List<Long> getByLangFromAndLangTo(long langFromId, long langToId);
    List<LangPairTranslator> getByLangFromAndLangTo(LanguageEntity langFrom, LanguageEntity langTo);
}

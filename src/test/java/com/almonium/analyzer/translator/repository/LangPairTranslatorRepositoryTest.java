package com.almonium.analyzer.translator.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.almonium.analyzer.translator.model.entity.LangPairTranslatorMapping;
import com.almonium.analyzer.translator.model.entity.Translator;
import com.almonium.analyzer.translator.model.enums.Language;
import com.almonium.config.PostgresContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@DataJpaTest
@ImportTestcontainers(PostgresContainer.class)
class LangPairTranslatorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LangPairTranslatorRepository repository;

    @DisplayName("Finds the providers of a language pair by its enum languages, in priority order")
    @Test
    void findsProvidersOfPairInPriorityOrder() {
        Translator yandex = translator("YANDEX");
        Translator google = translator("GOOGLE");
        map(Language.EN, Language.UK, google, 2);
        map(Language.EN, Language.UK, yandex, 1);
        map(Language.DE, Language.UK, google, 1);
        entityManager.flush();
        entityManager.clear();

        assertThat(repository.findProviderNames(Language.EN, Language.UK)).containsExactly("YANDEX", "GOOGLE");
        assertThat(repository.findProviderNames(Language.UK, Language.EN)).isEmpty();
    }

    private Translator translator(String name) {
        return entityManager
                .getEntityManager()
                .createQuery("select t from Translator t where t.name = :name", Translator.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    private void map(Language source, Language target, Translator translator, int priority) {
        entityManager.persist(new LangPairTranslatorMapping(source, target, translator.getId(), translator, priority));
    }
}

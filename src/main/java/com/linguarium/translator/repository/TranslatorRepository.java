package com.linguarium.translator.repository;

import com.linguarium.translator.model.Translator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslatorRepository extends JpaRepository<Translator, Long> {
    Optional<Translator> getByName(String name);

    default Translator getYandex() {
        return this.getByName("YANDEX").orElseThrow();
    }

    default Translator getGoogle() {
        return this.getByName("GOOGLE").orElseThrow();
    }
}

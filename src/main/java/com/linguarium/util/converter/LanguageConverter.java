package com.linguarium.util.converter;

import com.linguarium.translator.model.Language;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(Language nodeType) {
        return nodeType.getCode();
    }

    @Override
    public Language convertToEntityAttribute(String dbData) {
        for (Language nodeType : Language.values()) {
            if (nodeType.getCode().equals(dbData)) {
                return nodeType;
            }
        }

        throw new IllegalArgumentException("Unknown database value: " + dbData);
    }
}

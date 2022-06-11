package com.linguatool.model.entity.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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

        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}

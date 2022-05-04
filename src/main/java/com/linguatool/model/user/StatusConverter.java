package com.linguatool.model.user;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, String> {

    @Override
    public String convertToDatabaseColumn(Status nodeType) {
        return nodeType.getCode();
    }

    @Override
    public Status convertToEntityAttribute(String dbData) {
        for (Status nodeType : Status.values()) {
            if (nodeType.getCode().equals(dbData)) {
                return nodeType;
            }
        }

        throw new IllegalArgumentException("Unknown database value:" + dbData);
    }
}
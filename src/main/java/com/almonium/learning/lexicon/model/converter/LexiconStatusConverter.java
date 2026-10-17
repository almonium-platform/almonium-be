package com.almonium.learning.lexicon.model.converter;

import com.almonium.learning.lexicon.model.enums.LexiconStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/** Stores a status as its lowercase label, the vocabulary the lexicon tables use. */
@Converter(autoApply = true)
public class LexiconStatusConverter implements AttributeConverter<LexiconStatus, String> {
    @Override
    public String convertToDatabaseColumn(LexiconStatus status) {
        return status == null ? null : status.label();
    }

    @Override
    public LexiconStatus convertToEntityAttribute(String label) {
        return label == null ? null : LexiconStatus.fromLabel(label);
    }
}
